package com.cjgmj.testJpaSpecification.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * It contains:
 * <ul>
 * <li>dateFrom: attribute's name from request to filter by lower date.</li>
 * <li>dateUp: attribute's name from request to filter by greater date.</li>
 * <li>attribute: attribute in object.
 * <li>
 * </ul>
 * 
 * @author cjgmj
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateFilter {

	private String dateFrom;
	private String dateUp;
	private String attribute;

}
