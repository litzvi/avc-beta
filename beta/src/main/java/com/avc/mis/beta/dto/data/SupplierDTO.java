/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.HashSet;
import java.util.Set;

import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.SupplyCategory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying Supplier entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class SupplierDTO extends CompanyDTO {
	
	private Set<SupplyCategory> supplyCategories = new HashSet<>();
	
	public SupplierDTO(@NonNull Supplier supplier, boolean hasContacts) {
		super(supplier, hasContacts);
		this.supplyCategories.addAll(supplier.getSupplyCategories());
	}
	
	
	
}
