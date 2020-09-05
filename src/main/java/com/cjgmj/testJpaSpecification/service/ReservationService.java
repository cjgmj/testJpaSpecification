package com.cjgmj.testJpaSpecification.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cjgmj.testJpaSpecification.dto.ReservationPlainDTO;
import com.cjgmj.testJpaSpecification.entity.ReservationEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;

public interface ReservationService {

	ReservationEntity getReservation(Long id);

	Page<ReservationEntity> getReservations(FilterRequest filter);

	List<ReservationEntity> getReservations();

	void saveReservation(ReservationEntity reservation);

	void deleteReservation(Long id);

	List<ReservationPlainDTO> getReservationsPlain();

}
