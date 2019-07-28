package com.cjgmj.testJpaSpecification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjgmj.testJpaSpecification.entity.Reservation;
import com.cjgmj.testJpaSpecification.entity.id.ReservationId;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, ReservationId> {

}
