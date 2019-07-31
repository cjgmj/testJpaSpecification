package com.cjgmj.testJpaSpecification.dto.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cjgmj.testJpaSpecification.dto.ReservationDTO;
import com.cjgmj.testJpaSpecification.entity.ReservationEntity;

public class ReservationConverter {

	public static ReservationDTO convertToDto(ReservationEntity entity) {
		if (entity == null) {
			return null;
		}

		ReservationDTO dto = new ReservationDTO();

		dto.setId(entity.getId());
		dto.setPerson(PersonConverter.convertToDto(entity.getPerson()));
		dto.setRoom(RoomConverter.convertToDto(entity.getRoom()));

		LocalDateTime reservationDate = entity.getReservationDate();

		if (reservationDate != null) {
			dto.setReservationDate(reservationDate.toLocalDate());
		}

		dto.setDescription(entity.getDescription());

		return dto;
	}

	public static ReservationEntity convertToEntity(ReservationDTO dto) {
		if (dto == null) {
			return null;
		}
		ReservationEntity entity = new ReservationEntity();

		entity.setId(dto.getId());
		entity.setPerson(PersonConverter.convertToEntity(dto.getPerson()));
		entity.setRoom(RoomConverter.convertToEntity(dto.getRoom()));

		LocalDate reservationDate = dto.getReservationDate();

		if (reservationDate != null) {
			entity.setReservationDate(reservationDate.atStartOfDay());
		}

		entity.setDescription(dto.getDescription());

		return entity;
	}

}
