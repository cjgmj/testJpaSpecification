package com.cjgmj.testJpaSpecification.entity.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.cjgmj.testJpaSpecification.entity.Person;
import com.cjgmj.testJpaSpecification.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ReservationId implements Serializable {

	private static final long serialVersionUID = -7106945122454519402L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Person person;

	@ManyToOne(fetch = FetchType.LAZY)
	private Room room;
}
