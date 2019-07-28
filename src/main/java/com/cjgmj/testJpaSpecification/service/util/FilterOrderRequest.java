package com.cjgmj.testJpaSpecification.service.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.OrderRequest;
import com.cjgmj.testJpaSpecification.filter.SearchRequest;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;

@Component
public class FilterOrderRequest<T> {

	public Specification<T> filter(FilterRequest filter) {
		return (obj, cq, cb) -> {
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

			cq.orderBy(orders.toArray(new Order[orders.size()]));
			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

	public Predicate getDatePredicate(CriteriaBuilder cb, Root<?> obj, SearchRequest dateFrom, SearchRequest dateUp,
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

	private Predicate getPredicate(CriteriaBuilder cb, Root<T> obj, String field, String value) {
		try {
			String pattern = AttributesFilter.LIKE.concat(value.toLowerCase()).concat(AttributesFilter.LIKE);

			if (!isDate(value)) {
				return cb.like(cb.lower(obj.get(field)), pattern);
			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AttributesFilter.FORMATDATE);
				LocalDateTime date = LocalDate.parse(value, formatter).atStartOfDay();
				return cb.equal(obj.<LocalDateTime>get(field), cb.literal(date));
			}
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private Order getOrder(CriteriaBuilder cb, Root<T> obj, String field, String value) {
		try {
			Expression<?> expression = obj.get(field);

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
