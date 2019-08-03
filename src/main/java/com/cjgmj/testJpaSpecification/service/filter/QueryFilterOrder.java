package com.cjgmj.testJpaSpecification.service.filter;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.OrderRequest;
import com.cjgmj.testJpaSpecification.filter.SearchRequest;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;
import com.cjgmj.testJpaSpecification.util.DateFilter;

/**
 * 
 * A filter which can be passed to a repository which is implementing
 * {@link JpaSpecificationExecutor}.
 *
 * @param <T> the type of elements in this QueryFilterOrder.
 * 
 * @author cjgmj
 * @version 1.3
 * 
 */
@Component
public class QueryFilterOrder<T> {

	/**
	 * 
	 * @param filter      a {@link FilterRequest} which contains the pagination,
	 *                    filter and order.
	 * @param allFilters  a list with all attributes. Can be {@literal null}.
	 * @param dateFilters a {@link DateFilter} list to filter between two dates. Can
	 *                    be {@literal null}.
	 * @return if filter is {@literal null} it will return null, if it will not
	 *         return a {@link Specification} filtering by filter attributes.
	 * 
	 * @since 1.0
	 * 
	 */
	public Specification<T> filter(FilterRequest filter, List<String> allFilters, List<DateFilter> dateFilters) {
		return (obj, cq, cb) -> {

			if (filter != null) {
				List<Predicate> predicatesAnd = new ArrayList<>();
				List<Predicate> predicatesOr = new ArrayList<>();
				List<Order> orders = new ArrayList<>();

				if (filter.getSearch() != null) {
					for (SearchRequest search : filter.getSearch()) {
						if (search.getField() != null) {
							Predicate predicate = getPredicate(cb, obj, search.getField(), search.getValue());

							if (predicate != null) {
								predicatesAnd.add(predicate);
							}
						}
					}
				}

				if (filter.getGlobalSearch() != null) {
					if (allFilters != null) {
						for (String f : allFilters) {
							Predicate predicate = getPredicate(cb, obj, f, filter.getGlobalSearch());

							if (predicate != null) {
								predicatesOr.add(predicate);
							}
						}
					}
				}

				if (filter.getOrder() != null) {
					for (OrderRequest order : filter.getOrder()) {
						if (order.getField() != null) {
							Order o = getOrder(cb, obj, order.getField(), order.getSort());

							if (o != null) {
								orders.add(o);
							}
						}
					}
				}

				if (dateFilters != null) {
					List<Predicate> listP = filterDate(cb, obj, filter, dateFilters);

					if (!listP.isEmpty()) {
						predicatesAnd.addAll(listP);
					}
				}

				cq.orderBy(orders.toArray(new Order[orders.size()]));

				if (predicatesAnd.isEmpty() && predicatesOr.isEmpty()) {
					return null;
				} else if (!predicatesAnd.isEmpty() && predicatesOr.isEmpty()) {
					return cb.and(predicatesAnd.toArray(new Predicate[predicatesAnd.size()]));
				} else if (predicatesAnd.isEmpty() && !predicatesOr.isEmpty()) {
					return cb.or(predicatesOr.toArray(new Predicate[predicatesOr.size()]));
				} else {
					cb.and(predicatesAnd.toArray(new Predicate[predicatesAnd.size()]));

					return cb.or(predicatesOr.toArray(new Predicate[predicatesOr.size()]));
				}

			}
			return null;
		};
	}

	/**
	 * 
	 * @param cb          CriteriaBuild used in
	 *                    {@link #filter(FilterRequest, List, List)}.
	 * @param obj         Root used in {@link #filter(FilterRequest, List, List)}.
	 * @param filter      a {@link FilterRequest} which contains the pagination,
	 *                    filter and order.
	 * @param dateFilters a {@link DateFilter} list to filter between two dates.
	 * @return a {@link Predicate} list filtering by dates.
	 * 
	 * @since 1.2
	 * 
	 */
	private List<Predicate> filterDate(CriteriaBuilder cb, Root<T> obj, FilterRequest filter,
			List<DateFilter> dateFilters) {
		List<Predicate> predicates = new ArrayList<>();
		SearchRequest dateFrom = null;
		SearchRequest dateUp = null;

		if (filter.getSearch() != null) {
			for (DateFilter dateFilter : dateFilters) {
				dateFrom = filter.getSearch().stream()
						.filter(search -> dateFilter.getDateFrom().equals(search.getField())).findAny().orElse(null);
				dateUp = filter.getSearch().stream().filter(search -> dateFilter.getDateUp().equals(search.getField()))
						.findAny().orElse(null);

				if (dateFrom != null || dateUp != null) {
					Predicate predicate = getDatePredicate(cb, obj, dateFrom, dateUp, dateFilter.getAttribute());

					if (predicate != null) {
						predicates.add(predicate);
					}
				}
			}
		}
		return predicates;
	}

	/**
	 * 
	 * @param cb       CriteriaBuild used in
	 *                 {@link #filter(FilterRequest, List, List)}.
	 * @param obj      Root used in {@link #filter(FilterRequest, List, List)}.
	 * @param dateFrom a {@link SearchRequest} which contain the value to filter.
	 * @param dateUpa  a {@link SearchRequest} which contain the value to filter.
	 * @param field    it is the attribute by which it will be filtered.
	 * @return a {@link Predicate} filtering by dates. If dateFrom is
	 *         {@literal null}, it will return dates lower or equal to dateUp. If
	 *         dateUp is {@literal null}, it will return dates greater or equal to
	 *         dateFrom. If dateFrom and dateUp is not {@literal null}, it will
	 *         return dates between dateFrom and dateUp.
	 * 
	 * @since 1.0
	 * 
	 */
	private Predicate getDatePredicate(CriteriaBuilder cb, Root<T> obj, SearchRequest dateFrom, SearchRequest dateUp,
			String field) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AttributesFilter.FORMATDATE);

		if (dateFrom != null && dateUp == null) {
			LocalDateTime date = LocalDate.parse(dateFrom.getValue(), formatter).atStartOfDay();
			return cb.greaterThanOrEqualTo(obj.<LocalDateTime>get(field), cb.literal(date));
		} else if (dateFrom == null && dateUp != null) {
			LocalDateTime date = LocalDate.parse(dateUp.getValue(), formatter).atStartOfDay();
			return cb.lessThanOrEqualTo(obj.<LocalDateTime>get(field), cb.literal(date));
		} else {
			LocalDateTime dateF = LocalDate.parse(dateFrom.getValue(), formatter).atStartOfDay();
			LocalDateTime dateU = LocalDate.parse(dateUp.getValue(), formatter).atStartOfDay();
			return cb.between(obj.<LocalDateTime>get(field), cb.literal(dateF), cb.literal(dateU));
		}
	}

	/**
	 * 
	 * @param cb    CriteriaBuild used in
	 *              {@link #filter(FilterRequest, List, List)}.
	 * @param obj   Root used in {@link #filter(FilterRequest, List, List)}.
	 * @param field attribute to get field to filter.
	 * @param value value to filter attribute.
	 * @return a {@link Predicate} filtering by attributes.
	 * 
	 * @since 1.0
	 * 
	 */
	@SuppressWarnings("unchecked")
	private Predicate getPredicate(CriteriaBuilder cb, Root<T> obj, String field, String value) {
		try {
			String pattern = AttributesFilter.LIKE
					.concat(Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""))
					.concat(AttributesFilter.LIKE);

			if (!isDate(value)) {
				return cb.like(caseInsensitiveAccent((Expression<String>) getExpression(field, cb, obj), cb), pattern);
			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AttributesFilter.FORMATDATE);
				LocalDateTime date = LocalDate.parse(value, formatter).atStartOfDay();
				return cb.equal(obj.<LocalDateTime>get(field), cb.literal(date));
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param expression a {@link Expression} to do it case insensitive.
	 * @param cb         CriteriaBuild used in
	 *                   {@link #filter(FilterRequest, List, List)}.
	 * @return a {@link Expression} case insensitive.
	 * 
	 * @since 1.1
	 * 
	 */
	private Expression<String> caseInsensitiveAccent(Expression<String> expression, CriteriaBuilder cb) {
		Expression<String> result = expression;
		result = cb.lower(result);
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("á"), cb.literal("a") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("à"), cb.literal("a") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ä"), cb.literal("a") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("â"), cb.literal("a") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("é"), cb.literal("e") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("è"), cb.literal("e") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ë"), cb.literal("e") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ê"), cb.literal("e") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("í"), cb.literal("i") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ì"), cb.literal("i") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ï"), cb.literal("i") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("î"), cb.literal("i") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ó"), cb.literal("o") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ò"), cb.literal("o") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ö"), cb.literal("o") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ô"), cb.literal("o") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ú"), cb.literal("u") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ù"), cb.literal("u") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ü"), cb.literal("u") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("û"), cb.literal("u") });
		result = cb.function(AttributesFilter.REPLACE, String.class,
				new Expression[] { result, cb.literal("ñ"), cb.literal("n") });
		return result;
	}

	/**
	 * 
	 * @param cb    CriteriaBuild used in
	 *              {@link #filter(FilterRequest, List, List)}.
	 * @param obj   Root used in {@link #filter(FilterRequest, List, List)}.
	 * @param field attribute to get field to order.
	 * @param value value to order attribute.
	 * @return a {@link Order} ordering by fields.
	 * 
	 * @since 1.0
	 * 
	 */
	private Order getOrder(CriteriaBuilder cb, Root<T> obj, String field, String value) {
		try {
			Expression<?> expression = getExpression(field, cb, obj);

			if (expression != null) {
				if (AttributesFilter.DESC.equals(value)) {
					return cb.desc(expression);
				} else {
					return cb.asc(expression);
				}
			}
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param field attribute to get Object's field.
	 * @param cb    CriteriaBuild used in
	 *              {@link #filter(FilterRequest, List, List)}.
	 * @param obj   Root used in {@link #filter(FilterRequest, List, List)}.
	 * @return expression with attribute in object.
	 * 
	 * @since 1.2
	 * 
	 */
	private Expression<?> getExpression(String field, CriteriaBuilder cb, Root<T> obj) {
		Expression<?> expression = null;

		String[] arr = field.split("[.]");

		if (arr.length == 1) {
			expression = obj.get(field);
		} else {
			Join<Object, Object> join = obj.join(arr[0]);
			for (int i = 1; i < arr.length - 1; i++) {
				join = join.join(arr[i]);
			}
			expression = join.get(arr[arr.length - 1]);
		}

		return expression;
	}

	/**
	 * 
	 * @param value string to format
	 * @return it return {@literal true} if value is date, if it will not return
	 *         {@literal false}.
	 * 
	 * @since 1.0
	 * 
	 */
	private Boolean isDate(String value) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AttributesFilter.FORMATDATE);
			LocalDate.parse(value, formatter);
			return true;
		} catch (NullPointerException | DateTimeParseException e) {
			return false;
		}
	}

}
