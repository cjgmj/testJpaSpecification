package com.cjgmj.testJpaSpecification.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cjgmj.testJpaSpecification.entity.ReservationEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;

public interface ReservationService {

	public ReservationEntity getReservation(Long id);

	public Page<ReservationEntity> getReservations(FilterRequest filter);

	public List<ReservationEntity> getReservations();

	public void saveReservation(ReservationEntity reservation);

	public void deleteReservation(Long id);

}
