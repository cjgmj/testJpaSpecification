package com.cjgmj.testJpaSpecification.dto.converter;

import com.cjgmj.testJpaSpecification.dto.RoomDTO;
import com.cjgmj.testJpaSpecification.entity.RoomEntity;

public class RoomConverter {

	public static RoomDTO convertToDto(RoomEntity entity) {
		if (entity == null) {
			return null;
		}

		final RoomDTO dto = new RoomDTO();

		dto.setId(entity.getId());
		dto.setFloor(entity.getFloor());
		dto.setNumber(entity.getNumber());
		dto.setName(entity.getName());
		dto.setSize(entity.getSize());
		dto.setTv(entity.getTv());
		dto.setProjector(entity.getProjector());
		dto.setSpeaker(entity.getSpeaker());

		return dto;
	}

	public static RoomEntity convertToEntity(RoomDTO dto) {
		if (dto == null) {
			return null;
		}

		final RoomEntity entity = new RoomEntity();

		entity.setId(dto.getId());
		entity.setFloor(dto.getFloor());
		entity.setNumber(dto.getNumber());
		entity.setName(dto.getName());
		entity.setSize(dto.getSize());
		entity.setTv(dto.getTv());
		entity.setProjector(dto.getProjector());
		entity.setSpeaker(dto.getSpeaker());

		return entity;
	}

}
