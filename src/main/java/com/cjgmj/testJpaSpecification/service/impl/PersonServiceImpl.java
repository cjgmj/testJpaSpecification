package com.cjgmj.testJpaSpecification.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
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
import com.cjgmj.testJpaSpecification.repository.PersonRepository;
import com.cjgmj.testJpaSpecification.service.PersonService;
import com.cjgmj.testJpaSpecification.util.FieldFilter;

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
			Predicate predicate = null;

			if (FieldFilter.BIRTHDATE.equals(filter.getSearch().getField())) {
				predicate = getDatePredicate(cb, person, filter.getSearch().getValue());
			} else {
				predicate = getPredicate(cb, person, filter.getSearch().getField(), filter.getSearch().getValue());
			}

			if (predicate != null) {
				predicates.add(predicate);
			}

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

	private Predicate getPredicate(CriteriaBuilder cb, Root<Person> person, String field, String value) {
		String pattern = "%" + value + "%";

		switch (field) {
		case FieldFilter.NAME:
			return cb.like(person.get("name"), pattern);
		case FieldFilter.SURNAME:
			return cb.like(person.get("surname"), pattern);
		case FieldFilter.JOB:
			return cb.like(person.get("job"), pattern);
		case FieldFilter.EMAIL:
			return cb.like(person.get("email"), pattern);
		default:
			return null;
		}
	}

	private Predicate getDatePredicate(CriteriaBuilder cb, Root<Person> person, String value) {
		if (value == null) {
			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDateTime date = LocalDate.parse(value, formatter).atStartOfDay();

		return cb.equal(person.<LocalDateTime>get("birthdate"), cb.literal(date));
	}

}
