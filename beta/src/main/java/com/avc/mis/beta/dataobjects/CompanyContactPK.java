/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyContactPK implements Serializable {
	
	private Person person;
	private Company company;
}
