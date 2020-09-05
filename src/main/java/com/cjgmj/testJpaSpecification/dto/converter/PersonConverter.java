package com.cjgmj.testJpaSpecification.dto.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cjgmj.testJpaSpecification.dto.PersonDTO;
import com.cjgmj.testJpaSpecification.entity.PersonEntity;

public class PersonConverter {

	public static PersonDTO convertToDto(PersonEntity entity) {
		if (entity == null) {
			return null;
		}

		final PersonDTO dto = new PersonDTO();

		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setSurname(entity.getSurname());
		dto.setJob(entity.getJob());
		dto.setEmail(entity.getEmail());

		final LocalDateTime birthdate = entity.getBirthdate();

		if (birthdate != null) {
			dto.setBirthdate(birthdate.toLocalDate());
		}

		return dto;
	}

	public static PersonEntity convertToEntity(PersonDTO dto) {
		if (dto == null) {
			return null;
		}

		final PersonEntity entity = new PersonEntity();

		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setSurname(dto.getSurname());
		entity.setJob(dto.getJob());
		entity.setEmail(dto.getEmail());

		final LocalDate birthdate = dto.getBirthdate();

		if (birthdate != null) {
			entity.setBirthdate(birthdate.atStartOfDay());
		}

		return entity;
	}

}
