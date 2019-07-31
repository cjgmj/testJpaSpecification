package com.cjgmj.testJpaSpecification.service;

import org.springframework.data.domain.Page;

import com.cjgmj.testJpaSpecification.entity.PersonEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;

public interface PersonService {

	public Page<PersonEntity> getPersons(FilterRequest filter);

}
