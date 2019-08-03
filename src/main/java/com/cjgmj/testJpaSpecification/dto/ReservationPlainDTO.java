package com.cjgmj.testJpaSpecification.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPlainDTO implements Serializable {

	private static final long serialVersionUID = 255752436814333761L;

	private Long id;
	private String personName;
	private String roomName;
	private LocalDate reservationDate;
	private String description;

}
