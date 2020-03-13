/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Set;

import com.avc.mis.beta.entities.data.Supplier;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class SupplierBasic implements Serializable {
	@EqualsAndHashCode.Include
	private Integer id;
	
	@JsonIgnore
	private String name;
	
	public SupplierBasic(@NonNull Supplier supplier) {
		this.id = supplier.getId();
		this.name = supplier.getName();
	}
	
	public String getValue() {
		return getName();
	}
}
