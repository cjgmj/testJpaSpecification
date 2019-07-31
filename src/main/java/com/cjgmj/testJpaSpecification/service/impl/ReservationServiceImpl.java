package com.cjgmj.testJpaSpecification.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cjgmj.testJpaSpecification.entity.Reservation;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.PaginationRequest;
import com.cjgmj.testJpaSpecification.filter.SearchRequest;
import com.cjgmj.testJpaSpecification.repository.ReservationRepository;
import com.cjgmj.testJpaSpecification.service.ReservationService;
import com.cjgmj.testJpaSpecification.service.util.FilterOrderRequest;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private FilterOrderRequest<Reservation> filterOrderRequest;

	@Override
	public Reservation getReservation(Long id) {
		return reservationRepository.findById(id).orElse(null);
	}

	@Override
	public Page<Reservation> getReservations(FilterRequest filter) {
		PaginationRequest page = filter.getPage();
		Specification<Reservation> f = filterOrderRequest.filter(formatSearch(filter), null);

		return reservationRepository.findAll(f, PageRequest.of(page.getPage(), page.getPageSize()));
	}

	@Override
	public List<Reservation> getReservations() {
		return reservationRepository.findAll();
	}

	@Override
	public void saveReservation(Reservation reservation) {
		reservationRepository.save(reservation);
	}

	@Override
	public void deleteReservation(Long id) {
		reservationRepository.deleteById(id);
	}

	private FilterRequest formatSearch(FilterRequest filter) {
		List<SearchRequest> searchR = filter.getSearch();
		if (searchR != null) {
			for (SearchRequest search : searchR) {
				if (search.getField() != null) {
					switch (search.getField()) {
					case AttributesFilter.PERSONNAME:
						search.setField("person.name");
						break;
					case AttributesFilter.PERSONSURNAME:
						search.setField("person.surname");
						break;
					case AttributesFilter.ROOMNUMBER:
						search.setField("room.number");
						break;
					case AttributesFilter.ROOMNAME:
						search.setField("room.name");
						break;
					case AttributesFilter.RESERVATIONDATE:
						search.setField("reservationDate");
						break;
					case AttributesFilter.DESCRIPTION:
						search.setField("description");
						break;
					default:
						break;
					}
				}
			}
		}

		return filter;
	}

}
