package com.cjgmj.testJpaSpecification.service;

import org.springframework.data.domain.Page;

import com.cjgmj.testJpaSpecification.entity.Person;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;

public interface PersonService {

	public Page<Person> getPersons(FilterRequest filter);

}
