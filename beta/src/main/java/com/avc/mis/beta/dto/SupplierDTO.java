/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dataobjects.SupplyCategory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class SupplierDTO extends CompanyDTO implements Serializable {
	
	private Set<SupplyCategory> supplyCategories = new HashSet<>();
	
	/**
	 * @param supplier
	 */
	public SupplierDTO(Supplier supplier) {
		super(supplier);
		this.supplyCategories.addAll(supplier.getSupplyCategories());
	}
	
	
	
}
