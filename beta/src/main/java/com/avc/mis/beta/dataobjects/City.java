/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class City {
	private int id;
	private String name;
	private int countryId;
}
