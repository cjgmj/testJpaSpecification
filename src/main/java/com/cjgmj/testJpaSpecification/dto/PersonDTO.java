package com.cjgmj.testJpaSpecification.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO implements Serializable {

	private static final long serialVersionUID = 7178418822977930438L;

	private Long id;
	private String name;
	private String surname;
	private String job;
	private String email;
	private LocalDate birthdate;

}
