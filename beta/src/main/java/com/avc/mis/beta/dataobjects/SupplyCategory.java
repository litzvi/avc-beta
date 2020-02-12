/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class SupplyCategory {
	
	private int id;
	@NonNull private String name;
	
	
	
}
