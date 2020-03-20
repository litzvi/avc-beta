/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.HashSet;
import java.util.Set;

import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.data.SupplyCategory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class SupplierDTO extends CompanyDTO {
	
	private Set<SupplyCategory> supplyCategories = new HashSet<>();
	
	/**
	 * @param supplier
	 */
	public SupplierDTO(@NonNull Supplier supplier) {
		super(supplier);
		this.supplyCategories.addAll(supplier.getSupplyCategories());
	}
	
	
	
}
