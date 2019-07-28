package com.cjgmj.testJpaSpecification.service.impl;

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

			Predicate predicate = getPredicate(cb, person, filter.getSearch().getField(),
					filter.getSearch().getValue());

			if (predicate != null) {
				predicates.add(predicate);
			}

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

	private Predicate getPredicate(CriteriaBuilder cb, Root<Person> person, String field, String value) {
		String pattern = "%" + value + "%";

		switch (field) {
		case "name":
			return cb.like(person.get("name"), pattern);
		default:
			return null;
		}
	}

}
