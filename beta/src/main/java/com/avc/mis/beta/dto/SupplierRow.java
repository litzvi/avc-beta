/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.avc.mis.beta.dataobjects.Email;
import com.avc.mis.beta.dataobjects.Phone;
import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dataobjects.SupplyCategory;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class SupplierRow implements Serializable {
	
	private Integer id;
	private String name;
	private Object[] phones;
	private Object[] emails;
	private Object[] supplyCategories;
	
	public SupplierRow(Supplier supplier) {
		this.id = supplier.getId();
		this.name = supplier.getName();
		phones = supplier.getContactDetails().getPhones().stream().map(Phone::getName).toArray();
		emails = supplier.getContactDetails().getEmails().stream().map(Email::getName).toArray();
		supplyCategories = supplier.getSupplyCategories().stream().map(SupplyCategory::getName).toArray();

	}
}
