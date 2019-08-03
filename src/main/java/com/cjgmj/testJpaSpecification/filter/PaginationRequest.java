package com.cjgmj.testJpaSpecification.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * It contains:
 * <ul>
 * <li>page: page index.</li>
 * <li>pageSize: the size of the page.</li>
 * </ul>
 * 
 * @author cjgmj
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

	private Integer page;
	private Integer pageSize;

}
