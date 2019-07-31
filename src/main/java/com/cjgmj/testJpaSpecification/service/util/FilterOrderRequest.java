package com.cjgmj.testJpaSpecification.service.util;

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
import org.springframework.stereotype.Component;

import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.OrderRequest;
import com.cjgmj.testJpaSpecification.filter.SearchRequest;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;
import com.cjgmj.testJpaSpecification.util.DateFilter;

@Component
public class FilterOrderRequest<T> {

	public Specification<T> filter(FilterRequest filter, List<DateFilter> dateFilters) {
		return (obj, cq, cb) -> {

			if (filter != null) {
				List<Predicate> predicates = new ArrayList<>();
				List<Order> orders = new ArrayList<>();

				if (filter.getSearch() != null) {
					for (SearchRequest search : filter.getSearch()) {
						if (search.getField() != null) {
							Predicate predicate = getPredicate(cb, obj, search.getField(), search.getValue());

							if (predicate != null) {
								predicates.add(predicate);
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

				List<Predicate> listP = filterDate(cb, obj, filter, dateFilters);

				if (!listP.isEmpty()) {
					predicates.addAll(listP);
				}

				cq.orderBy(orders.toArray(new Order[orders.size()]));

				if (predicates.isEmpty()) {
					return null;
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			return null;
		};
	}

	private List<Predicate> filterDate(CriteriaBuilder cb, Root<T> obj, FilterRequest filter,
			List<DateFilter> dateFilters) {
		List<Predicate> predicates = new ArrayList<>();
		SearchRequest dateFrom = null;
		SearchRequest dateUp = null;

		if (filter.getSearch() != null && dateFilters != null) {
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
