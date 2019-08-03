package com.cjgmj.testJpaSpecification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cjgmj.testJpaSpecification.dto.ReservationPlainDTO;
import com.cjgmj.testJpaSpecification.entity.ReservationEntity;

@Repository
public interface ReservationRepository
		extends JpaRepository<ReservationEntity, Long>, JpaSpecificationExecutor<ReservationEntity> {

	@Query(nativeQuery = true)
	List<ReservationPlainDTO> getReservationsPlain();

}
