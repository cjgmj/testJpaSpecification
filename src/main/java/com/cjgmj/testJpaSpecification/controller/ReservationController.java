package com.cjgmj.testJpaSpecification.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjgmj.testJpaSpecification.dto.ReservationDTO;
import com.cjgmj.testJpaSpecification.dto.converter.ReservationConverter;
import com.cjgmj.testJpaSpecification.entity.ReservationEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.service.ReservationService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping("")
	public List<ReservationDTO> getReservations() {
		return reservationService.getReservations().stream().map(r -> ReservationConverter.convertToDto(r))
				.collect(Collectors.toList());
	}

	@PostMapping("")
	public Page<ReservationDTO> getReservations(@RequestBody FilterRequest filter) {
		Page<ReservationEntity> reservations = reservationService.getReservations(filter);
		List<ReservationDTO> listReservations = reservations.getContent().stream()
				.map(r -> ReservationConverter.convertToDto(r)).collect(Collectors.toList());
		return new PageImpl<ReservationDTO>(listReservations, reservations.getPageable(),
				reservations.getTotalElements());
	}

	@GetMapping("/{id}")
	public ReservationDTO getReservationById(@PathVariable Long id) {
		return ReservationConverter.convertToDto(reservationService.getReservation(id));
	}

	@PostMapping("/save")
	public void saveReservation(@RequestBody ReservationDTO reservation) {
		reservationService.saveReservation(ReservationConverter.convertToEntity(reservation));
	}

	@DeleteMapping("/delete/{id}")
	public void deleteReservation(@PathVariable Long id) {
		reservationService.deleteReservation(id);
	}

}
