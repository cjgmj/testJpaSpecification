package com.cjgmj.testJpaSpecification.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "reservations" })
@Entity
@Table(name = "rooms", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Room implements Serializable {

	private static final long serialVersionUID = 652569323031510114L;

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "floor", nullable = false)
	private Long floor;

	@Column(name = "number", nullable = false)
	private Long number;

	@Column(name = "name", nullable = true)
	private String name;

	@Column(name = "size", nullable = false)
	private Long size;

	@Column(name = "tv", nullable = true)
	private Boolean tv;

	@Column(name = "projector", nullable = true)
	private Boolean projector;

	@Column(name = "speaker", nullable = true)
	private Boolean speaker;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
	private Set<Reservation> reservations;

}
