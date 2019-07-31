package com.cjgmj.testJpaSpecification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjgmj.testJpaSpecification.entity.Reservation;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.service.ReservationService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping("")
	public List<Reservation> getReservations() {
		return reservationService.getReservations();
	}

	@PostMapping("")
	public Page<Reservation> getReservations(@RequestBody FilterRequest filter) {
		return reservationService.getReservations(filter);
	}

	@GetMapping("/{id}")
	public Reservation getReservationById(@PathVariable Long id) {
		return reservationService.getReservation(id);
	}

	@PostMapping("/save")
	public void saveReservation(@RequestBody Reservation reservation) {
		reservationService.saveReservation(reservation);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteReservation(@PathVariable Long id) {
		reservationService.deleteReservation(id);
	}

}
