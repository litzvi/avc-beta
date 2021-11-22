/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.util.Set;

import com.avc.mis.beta.dto.BasicDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Row in list of suppliers report.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class SupplierRow extends BasicDTO {
	
	private String name;
	private Set<String> phones;
	private Set<String> emails;
	private Set<String> supplyCategories;
	
	@JsonIgnore
	private Integer contactDetailsId; //for joining with sets
	
	public SupplierRow(Integer id, String name, Integer contactDetailsId) {
		super(id);
		this.name = name;
		this.contactDetailsId = contactDetailsId;
	}
	
	
}
