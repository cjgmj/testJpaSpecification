package com.cjgmj.testJpaSpecification.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

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
import com.cjgmj.testJpaSpecification.service.util.FilterOrderRequest;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private FilterOrderRequest<Person> filterOrderRequest;

	@Override
	public Page<Person> getPersons(FilterRequest filter) {
		PaginationRequest page = filter.getPage();
		Specification<Person> f = filterOrderRequest.filter(filter).and(filter(filter));
		return personRepository.findAll(f, PageRequest.of(page.getPage(), page.getPageSize()));
	}

	private Specification<Person> filter(FilterRequest filter) {
		return (person, cq, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			SearchRequest birthdateFrom = null;
			SearchRequest birthdateUp = null;

			if (filter.getSearch() != null) {
				birthdateFrom = filter.getSearch().stream()
						.filter(search -> AttributesFilter.BIRTHDATEFROM.equals(search.getField())).findAny()
						.orElse(null);
				birthdateUp = filter.getSearch().stream()
						.filter(search -> AttributesFilter.BIRTHDATEUP.equals(search.getField())).findAny()
						.orElse(null);

				if (birthdateFrom != null || birthdateUp != null) {
					Predicate predicate = filterOrderRequest.getDatePredicate(cb, person, birthdateFrom, birthdateUp,
							AttributesFilter.BIRTHDATE);

					if (predicate != null) {
						predicates.add(predicate);
					}
				}
			}
			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}
