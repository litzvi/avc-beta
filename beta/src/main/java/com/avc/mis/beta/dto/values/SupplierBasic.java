/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.DataDTO;
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
public class SupplierBasic extends DataDTO {
	
	@JsonIgnore
	@ToString.Exclude
	String name;
	
	public SupplierBasic(Integer id, Long version, String name) {
		super(id, version);
		this.name = name;
	}
	
	public SupplierBasic(@NonNull Supplier supplier) {
		super(supplier.getId(), supplier.getVersion());
		this.name = supplier.getName();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getName();
	}
}
