package com.cjgmj.testJpaSpecification.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cjgmj.testJpaSpecification.dto.ReservationPlainDTO;
import com.cjgmj.testJpaSpecification.entity.ReservationEntity;
import com.cjgmj.testJpaSpecification.filter.FilterRequest;
import com.cjgmj.testJpaSpecification.filter.PaginationRequest;
import com.cjgmj.testJpaSpecification.filter.SearchRequest;
import com.cjgmj.testJpaSpecification.repository.ReservationRepository;
import com.cjgmj.testJpaSpecification.service.ReservationService;
import com.cjgmj.testJpaSpecification.service.filter.QueryFilterOrder;
import com.cjgmj.testJpaSpecification.util.AttributesFilter;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private QueryFilterOrder<ReservationEntity> filterOrderRequest;

	@Override
	public ReservationEntity getReservation(Long id) {
		return this.reservationRepository.findById(id).orElse(null);
	}

	@Override
	public Page<ReservationEntity> getReservations(FilterRequest filter) {
		final PaginationRequest page = filter.getPage();
		final Specification<ReservationEntity> f = this.filterOrderRequest.filter(this.formatSearch(filter), null,
				null);

		return this.reservationRepository.findAll(f, PageRequest.of(page.getPage(), page.getPageSize()));
	}

	@Override
	public List<ReservationEntity> getReservations() {
		return this.reservationRepository.findAll();
	}

	@Override
	public void saveReservation(ReservationEntity reservation) {
		this.reservationRepository.save(reservation);
	}

	@Override
	public void deleteReservation(Long id) {
		this.reservationRepository.deleteById(id);
	}

	private FilterRequest formatSearch(FilterRequest filter) {
		final List<SearchRequest> searchR = filter.getSearch();
		if (searchR != null) {
			for (final SearchRequest search : searchR) {
				if (search.getField() != null) {
					switch (search.getField()) {
					case AttributesFilter.PERSON_NAME:
						search.setField("person.name");
						break;
					case AttributesFilter.PERSON_SURNAME:
						search.setField("person.surname");
						break;
					case AttributesFilter.ROOM_NUMBER:
						search.setField("room.number");
						break;
					case AttributesFilter.ROOM_NAME:
						search.setField("room.name");
						break;
					case AttributesFilter.RESERVATION_DATE:
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

	@Override
	public List<ReservationPlainDTO> getReservationsPlain() {
		return this.reservationRepository.getReservationsPlain();
	}

}
