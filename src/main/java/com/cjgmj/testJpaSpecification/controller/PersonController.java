package com.cjgmj.testJpaSpecification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjgmj.testJpaSpecification.entity.Person;
import com.cjgmj.testJpaSpecification.service.PersonService;

@RestController
@RequestMapping("/persona")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping("")
	public List<Person> getPersons() {
		return personService.getPersons();
	}
}
