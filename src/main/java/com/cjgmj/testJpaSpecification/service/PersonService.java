package com.cjgmj.testJpaSpecification.service;

import org.springframework.data.domain.Page;

import com.cjgmj.testJpaSpecification.entity.PersonEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;

public interface PersonService {

	Page<PersonEntity> getPersons(FilterRequest filter);

}
