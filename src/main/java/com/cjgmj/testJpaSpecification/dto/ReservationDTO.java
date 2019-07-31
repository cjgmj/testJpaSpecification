package com.cjgmj.testJpaSpecification.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO implements Serializable {

	private static final long serialVersionUID = 2058964633077844885L;

	private Long id;
	private PersonDTO person;
	private RoomDTO room;
	private LocalDate reservationDate;
	private String description;

}
