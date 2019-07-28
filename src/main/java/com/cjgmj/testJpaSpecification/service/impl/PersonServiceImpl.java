package com.cjgmj.testJpaSpecification.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjgmj.testJpaSpecification.entity.Person;
import com.cjgmj.testJpaSpecification.repository.PersonRepository;
import com.cjgmj.testJpaSpecification.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;

	@Override
	public List<Person> getPersons() {
		return personRepository.findAll();
	}

}
