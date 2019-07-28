package com.cjgmj.testJpaSpecification.filter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

	private PaginationRequest page;

	private List<SearchRequest> search;

	private List<OrderRequest> order;

}
