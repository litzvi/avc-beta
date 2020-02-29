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
	private Set<Phone> phones = new HashSet<>();
	private Set<Email> emails = new HashSet<>();
	private Set<SupplyCategory> supplyCategories = new HashSet<>();
	
	public SupplierRow(Supplier supplier) {
		this.id = supplier.getId();
		this.name = supplier.getName();
		this.phones.addAll(supplier.getContactDetails().getPhones());
		this.emails.addAll(supplier.getContactDetails().getEmails());
		this.supplyCategories.addAll(supplier.getSupplyCategories());
		
	}
}
