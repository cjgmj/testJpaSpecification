package com.cjgmj.testJpaSpecification.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjgmj.testJpaSpecification.dto.PersonDTO;
import com.cjgmj.testJpaSpecification.dto.converter.PersonConverter;
import com.cjgmj.testJpaSpecification.entity.PersonEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@PostMapping("")
	public Page<PersonDTO> getPersons(@RequestBody FilterRequest filter) {
		Page<PersonEntity> persons = personService.getPersons(filter);
		List<PersonDTO> listPersons = persons.getContent().stream().map(p -> PersonConverter.convertToDto(p))
				.collect(Collectors.toList());
		return new PageImpl<PersonDTO>(listPersons, persons.getPageable(), persons.getTotalElements());
	}
}
