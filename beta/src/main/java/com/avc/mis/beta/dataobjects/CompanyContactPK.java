/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class CompanyContactPK implements Serializable {
	
	private Person person;
	private Integer companyId;
}
