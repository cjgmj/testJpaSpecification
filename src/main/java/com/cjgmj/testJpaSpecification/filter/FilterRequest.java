package com.cjgmj.testJpaSpecification.filter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * FilterRequest is the main class to get filter from request.
 * 
 * It contains:
 * <ul>
 * <li>page: a {@link PaginationRequest} object.</li>
 * <li>search: a {@link SearchRequest} list.</li>
 * <li>order: a {@link OrderRequest} list.</li>
 * <li>globalSearch: a string to filter by all attributes.</li>
 * </ul>
 * 
 * @author cjgmj
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

	private PaginationRequest page;
	private List<SearchRequest> search;
	private List<OrderRequest> order;
	private String globalSearch;

}
