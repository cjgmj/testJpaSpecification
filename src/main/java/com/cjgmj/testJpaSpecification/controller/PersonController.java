package com.cjgmj.testJpaSpecification.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjgmj.testJpaSpecification.dto.PersonDTO;
import com.cjgmj.testJpaSpecification.dto.converter.PersonConverter;
import com.cjgmj.testJpaSpecification.entity.PersonEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.TableFilter;
import com.cjgmj.testJpaSpecification.filter.TableHeader;
import com.cjgmj.testJpaSpecification.service.PersonService;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping("/tableHeader")
	public List<TableHeader> getTableHeaders() {
		List<TableHeader> headers = new ArrayList<>();

		headers.add(new TableHeader("id", "ID", Boolean.TRUE));
		headers.add(new TableHeader("name", "Nombre", Boolean.FALSE));
		headers.add(new TableHeader("surname", "Apellidos", Boolean.FALSE));
		headers.add(new TableHeader("job", "Puesto", Boolean.FALSE));
		headers.add(new TableHeader("email", "Email", Boolean.FALSE));
		headers.add(new TableHeader("birthdate", "Fecha de nacimiento", Boolean.FALSE));

		return headers;
	}

	@GetMapping("/tableFilter")
	public List<TableFilter> getTableFilters() {
		List<TableFilter> filters = new ArrayList<>();

		filters.add(new TableFilter(AttributesFilter.NAME, "Nombre", TableFilter.TYPE_TEXT, null));
		filters.add(new TableFilter(AttributesFilter.SURNAME, "Apellidos", TableFilter.TYPE_TEXT, null));
		filters.add(new TableFilter(AttributesFilter.JOB, "Puesto", TableFilter.TYPE_TEXT, null));
		filters.add(new TableFilter(AttributesFilter.EMAIL, "Email", TableFilter.TYPE_TEXT, null));
		filters.add(new TableFilter(AttributesFilter.BIRTHDATE, "Fecha de nacimiento", TableFilter.TYPE_DATE, "=="));
		filters.add(new TableFilter(AttributesFilter.BIRTHDATE_FROM, "Fecha de nacimiento desde", TableFilter.TYPE_DATE,
				">="));
		filters.add(new TableFilter(AttributesFilter.BIRTHDATE_UP, "Fecha de nacimiento hasta", TableFilter.TYPE_DATE,
				"<="));

		return filters;
	}

	@PostMapping("")
	public Page<PersonDTO> getPersons(@RequestBody FilterRequest filter) {
		Page<PersonEntity> persons = personService.getPersons(filter);
		List<PersonDTO> listPersons = persons.getContent().stream().map(p -> PersonConverter.convertToDto(p))
				.collect(Collectors.toList());
		return new PageImpl<PersonDTO>(listPersons, persons.getPageable(), persons.getTotalElements());
	}
}
