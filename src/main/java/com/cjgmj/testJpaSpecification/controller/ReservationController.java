package com.cjgmj.testJpaSpecification.controller;

import java.util.ArrayList;
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
import com.cjgmj.testJpaSpecification.dto.ReservationPlainDTO;
import com.cjgmj.testJpaSpecification.dto.converter.ReservationConverter;
import com.cjgmj.testJpaSpecification.entity.ReservationEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.TableFilter;
import com.cjgmj.testJpaSpecification.filter.TableHeader;
import com.cjgmj.testJpaSpecification.service.ReservationService;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping("/tableHeader")
	public List<TableHeader> getTableHeaders() {
		final List<TableHeader> headers = new ArrayList<>();

		headers.add(new TableHeader("id", "ID", Boolean.TRUE));
		headers.add(new TableHeader("person.name", "Nombre", Boolean.FALSE));
		headers.add(new TableHeader("person.surname", "Apellidos", Boolean.FALSE));
		headers.add(new TableHeader("room.name", "Nombre sala", Boolean.FALSE));
		headers.add(new TableHeader("room.floor", "Planta", Boolean.FALSE));
		headers.add(new TableHeader("room.number", "Número", Boolean.FALSE));
		headers.add(new TableHeader("reservationDate", "Fecha de reserva", Boolean.FALSE));
		headers.add(new TableHeader("description", "Descripción", Boolean.FALSE));

		return headers;
	}

	@GetMapping("/tableHeaderPlain")
	public List<TableHeader> getTableHeadersPlain() {
		final List<TableHeader> headers = new ArrayList<>();

		headers.add(new TableHeader("id", "ID", Boolean.TRUE));
		headers.add(new TableHeader("personName", "Nombre", Boolean.FALSE));
		headers.add(new TableHeader("roomName", "Nombre de la sala", Boolean.FALSE));
		headers.add(new TableHeader("reservationDate", "Fecha de reserva", Boolean.FALSE));
		headers.add(new TableHeader("description", "Descripción", Boolean.FALSE));

		return headers;
	}

	@GetMapping("/tableFilter")
	public List<TableFilter> getTableFilters() {
		final List<TableFilter> filters = new ArrayList<>();

		filters.add(new TableFilter(AttributesFilter.PERSON_NAME, "Nombre", TableFilter.TYPE_TEXT, null));
		filters.add(new TableFilter(AttributesFilter.PERSON_SURNAME, "Apellidos", TableFilter.TYPE_TEXT, null));
		filters.add(new TableFilter(AttributesFilter.ROOM_NUMBER, "Número de sala", TableFilter.TYPE_TEXT, null));
		filters.add(new TableFilter(AttributesFilter.ROOM_NAME, "Nombre de la sala", TableFilter.TYPE_TEXT, null));
		filters.add(
				new TableFilter(AttributesFilter.RESERVATION_DATE, "Fecha de reserva", TableFilter.TYPE_DATE, "=="));
		filters.add(new TableFilter(AttributesFilter.DESCRIPTION, "Descripción", TableFilter.TYPE_TEXT, null));

		return filters;
	}

	@GetMapping("")
	public List<ReservationDTO> getReservations() {
		return this.reservationService.getReservations().stream().map(r -> ReservationConverter.convertToDto(r))
				.collect(Collectors.toList());
	}

	@PostMapping("")
	public Page<ReservationDTO> getReservations(@RequestBody FilterRequest filter) {
		final Page<ReservationEntity> reservations = this.reservationService.getReservations(filter);
		final List<ReservationDTO> listReservations = reservations.getContent().stream()
				.map(r -> ReservationConverter.convertToDto(r)).collect(Collectors.toList());
		return new PageImpl<>(listReservations, reservations.getPageable(),
				reservations.getTotalElements());
	}

	@GetMapping("/{id}")
	public ReservationDTO getReservationById(@PathVariable Long id) {
		return ReservationConverter.convertToDto(this.reservationService.getReservation(id));
	}

	@PostMapping("/save")
	public void saveReservation(@RequestBody ReservationDTO reservation) {
		this.reservationService.saveReservation(ReservationConverter.convertToEntity(reservation));
	}

	@DeleteMapping("/delete/{id}")
	public void deleteReservation(@PathVariable Long id) {
		this.reservationService.deleteReservation(id);
	}

	@GetMapping("/plain")
	public List<ReservationPlainDTO> getReservationsPlain() {
		return this.reservationService.getReservationsPlain();
	}

}
