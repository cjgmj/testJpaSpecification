package com.cjgmj.testJpaSpecification.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cjgmj.testJpaSpecification.entity.Person;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.PaginationRequest;
import com.cjgmj.testJpaSpecification.filter.SearchRequest;
import com.cjgmj.testJpaSpecification.repository.PersonRepository;
import com.cjgmj.testJpaSpecification.service.PersonService;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;

	@Override
	public Page<Person> getPersons(FilterRequest filter) {
		PaginationRequest page = filter.getPage();
		return personRepository.findAll(filter(filter), PageRequest.of(page.getPage(), page.getPageSize()));
	}

	private Specification<Person> filter(FilterRequest filter) {
		return (person, cq, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			SearchRequest birthdateFrom = null;
			SearchRequest birthdateUp = null;

			if (!filter.getSearch().isEmpty()) {
				for (SearchRequest search : filter.getSearch()) {
					if (AttributesFilter.BIRTHDATEFROM.equals(search.getField())) {
						birthdateFrom = search;
					} else if (AttributesFilter.BIRTHDATEUP.equals(search.getField())) {
						birthdateUp = search;
					} else {
						if (search.getValue() != null) {
							Predicate predicate = getPredicate(cb, person, search.getField(), search.getValue());

							if (predicate != null) {
								predicates.add(predicate);
							}
						}
					}
				}

				if (birthdateFrom != null || birthdateUp != null) {
					Predicate predicate = getDatePredicate(cb, person, birthdateFrom, birthdateUp);

					if (predicate != null) {
						predicates.add(predicate);
					}
				}
			}

			if (filter.getOrder() != null) {
				Order order = getOrder(cb, person, filter.getOrder().getField(), filter.getOrder().getSort());

				if (order != null) {
					cq.orderBy(order);
				}
			}

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

	private Predicate getPredicate(CriteriaBuilder cb, Root<Person> person, String field, String value) {
		String pattern = "%" + value + "%";

		switch (field) {
		case AttributesFilter.NAME:
			return cb.like(person.get("name"), pattern);
		case AttributesFilter.SURNAME:
			return cb.like(person.get("surname"), pattern);
		case AttributesFilter.JOB:
			return cb.like(person.get("job"), pattern);
		case AttributesFilter.EMAIL:
			return cb.like(person.get("email"), pattern);
		case AttributesFilter.BIRTHDATE:
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime date = LocalDate.parse(value, formatter).atStartOfDay();
			return cb.equal(person.<LocalDateTime>get("birthdate"), cb.literal(date));
		default:
			return null;
		}
	}

	private Predicate getDatePredicate(CriteriaBuilder cb, Root<Person> person, SearchRequest birthdateFrom,
			SearchRequest birthdateUp) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		if (birthdateFrom != null && birthdateUp == null) {
			LocalDateTime date = LocalDate.parse(birthdateFrom.getValue(), formatter).atStartOfDay();
			return cb.greaterThanOrEqualTo(person.<LocalDateTime>get("birthdate"), cb.literal(date));
		} else if (birthdateFrom == null && birthdateUp != null) {
			LocalDateTime date = LocalDate.parse(birthdateUp.getValue(), formatter).atStartOfDay();
			return cb.lessThanOrEqualTo(person.<LocalDateTime>get("birthdate"), cb.literal(date));
		} else {
			LocalDateTime dateF = LocalDate.parse(birthdateFrom.getValue(), formatter).atStartOfDay();
			LocalDateTime dateU = LocalDate.parse(birthdateUp.getValue(), formatter).atStartOfDay();
			return cb.between(person.<LocalDateTime>get("birthdate"), cb.literal(dateF), cb.literal(dateU));
		}
	}

	private Order getOrder(CriteriaBuilder cb, Root<Person> person, String field, String value) {
		Expression<?> expression = null;

		switch (field) {
		case AttributesFilter.NAME:
			expression = person.get("name");
			break;
		case AttributesFilter.SURNAME:
			expression = person.get("surname");
			break;
		case AttributesFilter.JOB:
			expression = person.get("job");
			break;
		case AttributesFilter.EMAIL:
			expression = person.get("email");
			break;
		case AttributesFilter.BIRTHDATE:
			expression = person.<LocalDateTime>get("birthdate");
			break;
		}

		if (expression != null) {
			if (AttributesFilter.DESC.equals(value)) {
				return cb.desc(expression);
			} else {
				return cb.asc(expression);
			}
		}

		return null;
	}

}
