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
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = "reservations")
@Entity
@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Person implements Serializable {

	private static final long serialVersionUID = 8460232819362246412L;

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "surname", nullable = false)
	private String surname;

	@Column(name = "job", nullable = false)
	private String job;

	@Email
	@Column(name = "email", nullable = false)
	private String email;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.person")
	private Set<Reservation> reservations;

}
