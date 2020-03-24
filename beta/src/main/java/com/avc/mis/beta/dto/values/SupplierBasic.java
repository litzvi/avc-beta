/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
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
@EqualsAndHashCode(callSuper = true)
public class SupplierBasic extends ValueDTO {
	
	@JsonIgnore
	@ToString.Exclude
	String name;
	
	public SupplierBasic(Integer id, String name) {
		super(id);
		this.name = name;
	}
	
	public SupplierBasic(@NonNull Supplier supplier) {
		super(supplier.getId());
		this.name = supplier.getName();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getName();
	}
}
