package com.cjgmj.testJpaSpecification.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateFilter {

	private String dateFrom;
	private String dateUp;
	private String attribute;

}
