package com.cjgmj.testJpaSpecification.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * It contains:
 * <ul>
 * <li>key: attribute to filter.</li>
 * <li>label: header name.</li>
 * <li>type: filter type.</li>
 * </ul>
 * 
 * @author cjgmj
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableFilter {

	public static final String TYPE_TEXT = "text";
	public static final String TYPE_SELECT = "select";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_CHECKBOX = "checkbox";

	private String key;
	private String label;
	private String type;
	private String condition;

}
