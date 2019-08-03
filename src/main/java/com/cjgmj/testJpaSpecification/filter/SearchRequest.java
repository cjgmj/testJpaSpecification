package com.cjgmj.testJpaSpecification.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * It contains:
 * <ul>
 * <li>field: attribute to filter.</li>
 * <li>value: string to filter.</li>
 * </ul>
 * 
 * @author cjgmj
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

	private String field;
	private String value;

}
