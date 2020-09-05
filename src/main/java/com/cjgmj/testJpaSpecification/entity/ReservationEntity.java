package com.cjgmj.testJpaSpecification.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.PrePersist;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cjgmj.testJpaSpecification.dto.ReservationPlainDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = "createAt", allowGetters = true)
@Entity
@Table(name = "reservations", uniqueConstraints = @UniqueConstraint(columnNames = "id"))

@SqlResultSetMappings({
		@SqlResultSetMapping(name = "reservationPlain", classes = @ConstructorResult(targetClass = ReservationPlainDTO.class, columns = {
				@ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "personName", type = String.class),
				@ColumnResult(name = "roomName", type = String.class),
				@ColumnResult(name = "reservationDate", type = LocalDate.class),
				@ColumnResult(name = "description", type = String.class) })) })

@NamedNativeQueries({
		@NamedNativeQuery(name = "ReservationEntity.getReservationsPlain", resultSetMapping = "reservationPlain", query = "select re.id, concat(pe.name, "
				+ "concat(' '), pe.surname) as personName, concat('Sala ', concat(ro.name, concat(' (Planta: ', concat(ro.floor, concat(' - Número: ', "
				+ "concat(ro.number, ')')))))) as roomName, re.reservation_date as reservationDate, re.description from persons pe inner join reservations re "
				+ "on pe.id = re.person inner join rooms ro on re.room = ro.id") })
public class ReservationEntity implements Serializable {

	private static final long serialVersionUID = -126758419208139948L;

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person", nullable = false)
	private PersonEntity person;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room", nullable = false)
	private RoomEntity room;

	@Column(name = "reservationDate", nullable = false)
	private LocalDateTime reservationDate;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "createAt", nullable = false)
	private LocalDateTime createAt;

	@PrePersist
	private void prePersist() {
		this.setCreateAt(LocalDateTime.now());
	}

}
