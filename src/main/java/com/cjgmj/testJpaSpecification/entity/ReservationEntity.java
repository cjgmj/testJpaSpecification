package com.cjgmj.testJpaSpecification.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
		setCreateAt(LocalDateTime.now());
	}

}
