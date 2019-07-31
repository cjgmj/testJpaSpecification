package com.cjgmj.testJpaSpecification.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO implements Serializable {

	private static final long serialVersionUID = 6302369120875728844L;

	private Long id;
	private Long floor;
	private Long number;
	private String name;
	private Long size;
	private Boolean tv;
	private Boolean projector;
	private Boolean speaker;

}
