package com.cjgmj.testJpaSpecification.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cjgmj.testJpaSpecification.entity.Reservation;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;

public interface ReservationService {

	public Reservation getReservation(Long id);

	public Page<Reservation> getReservations(FilterRequest filter);

	public List<Reservation> getReservations();

	public void saveReservation(Reservation reservation);

	public void deleteReservation(Long id);

}
