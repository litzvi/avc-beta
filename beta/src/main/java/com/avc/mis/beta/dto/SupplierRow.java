/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	private List<String> phones;
	private List<String> emails;
	private List<String> supplyCategories;
	
	public SupplierRow(Supplier supplier) {
		this.id = supplier.getId();
		this.name = supplier.getName();
		phones = Arrays.asList(supplier.getContactDetails().getPhones()).stream()
				.map(Phone::getName).collect(Collectors.toList());
		emails = supplier.getContactDetails().getEmails().stream()
				.map(Email::getName).collect(Collectors.toList());
		supplyCategories = supplier.getSupplyCategories().stream()
				.map(SupplyCategory::getName).collect(Collectors.toList());

	}
}
