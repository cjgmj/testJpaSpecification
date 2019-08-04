package com.cjgmj.testJpaSpecification.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cjgmj.testJpaSpecification.entity.PersonEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.PaginationRequest;
import com.cjgmj.testJpaSpecification.filter.SearchRequest;
import com.cjgmj.testJpaSpecification.repository.PersonRepository;
import com.cjgmj.testJpaSpecification.service.PersonService;
import com.cjgmj.testJpaSpecification.service.filter.QueryFilterOrder;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;
import com.cjgmj.testJpaSpecification.util.DateFilter;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private QueryFilterOrder<PersonEntity> queryFilterOrder;

	@Override
	public Page<PersonEntity> getPersons(FilterRequest filter) {
		PaginationRequest page = filter.getPage();
		Specification<PersonEntity> f = queryFilterOrder.filter(formatSearch(filter), getAllFilters(), getDateFilter());

		return personRepository.findAll(f, PageRequest.of(page.getPage(), page.getPageSize()));
	}

	private FilterRequest formatSearch(FilterRequest filter) {
		List<SearchRequest> searchR = filter.getSearch();
		if (searchR != null) {
			for (SearchRequest search : searchR) {
				if (search.getField() != null) {
					switch (search.getField()) {
					case AttributesFilter.NAME:
						search.setField("name");
						break;
					case AttributesFilter.SURNAME:
						search.setField("surname");
						break;
					case AttributesFilter.JOB:
						search.setField("job");
						break;
					case AttributesFilter.EMAIL:
						search.setField("email");
						break;
					case AttributesFilter.BIRTHDATE:
						search.setField("birthdate");
						break;
					default:
						break;
					}
				}
			}
		}

		return filter;
	}

	private List<String> getAllFilters() {
		List<String> filters = new ArrayList<>();

		filters.add("name");
		filters.add("surname");
		filters.add("job");
		filters.add("email");
		filters.add("birthdate");

		return filters;
	}

	private List<DateFilter> getDateFilter() {
		List<DateFilter> arr = new ArrayList<>();

		arr.add(new DateFilter(AttributesFilter.BIRTHDATE_FROM, AttributesFilter.BIRTHDATE_UP, "birthdate"));

		return arr;
	}

}
