/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.io.Serializable;

import com.avc.mis.beta.entities.data.Supplier;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
public class SupplierBasic implements Serializable {
	
	@EqualsAndHashCode.Exclude
	Integer id;
	
	@JsonIgnore
	@ToString.Exclude
	String name;
	
	@lombok.experimental.Tolerate
	public SupplierBasic(@NonNull Supplier supplier) {
		this.id = supplier.getId();
		this.name = supplier.getName();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getName();
	}
}
