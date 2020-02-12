/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class SupplyCategory {
	
	private int id;
	private String name;
	/**
	 * @param id
	 * @param name
	 */
	public SupplyCategory(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	
}
