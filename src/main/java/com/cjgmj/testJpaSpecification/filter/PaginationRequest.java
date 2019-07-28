package com.cjgmj.testJpaSpecification.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

	private Integer page;
	private Integer pageSize;

}
