package com.cjgmj.testJpaSpecification.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.cjgmj.testJpaSpecification.entity.id.ReservationId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = "createAt", allowGetters = true)
@Entity
@Table(name = "reservations")
@AssociationOverrides({ @AssociationOverride(name = "pk.person", joinColumns = @JoinColumn(name = "person")),
		@AssociationOverride(name = "pk.room", joinColumns = @JoinColumn(name = "room")) })
public class Reservation implements Serializable {

	private static final long serialVersionUID = -126758419208139948L;

	@EmbeddedId
	private ReservationId pk;

	@Column(name = "reservationDate", nullable = false)
	private LocalDateTime reservationDate;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "createAt", nullable = false)
	private LocalDateTime createAt;

	public Person getPerson() {
		return this.pk.getPerson();
	}

	public void setPerson(Person person) {
		this.pk.setPerson(person);
	}

	public Room getRoom() {
		return this.pk.getRoom();
	}

	public void setRoom(Room room) {
		this.pk.setRoom(room);
	}

}
