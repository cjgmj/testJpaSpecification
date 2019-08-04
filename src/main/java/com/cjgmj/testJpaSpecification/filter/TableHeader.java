package com.cjgmj.testJpaSpecification.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * It contains:
 * <ul>
 * <li>key: attribute name in query.</li>
 * <li>label: header name.</li>
 * <li>isHidden: it determine if the column is hidden.</li>
 * </ul>
 * 
 * @author cjgmj
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableHeader {

	private String key;
	private String label;
	private Boolean isHidden;

}
