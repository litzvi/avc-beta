/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.DTOWithId;
import com.avc.mis.beta.entities.data.Email;
import com.avc.mis.beta.entities.data.Phone;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class SupplierRow extends DTOWithId {
	
	private String name;
	private Set<String> phones;
	private Set<String> emails;
	private Set<String> supplyCategories;
	
	@JsonIgnore
	private Integer contactDetailsId; //for joining with sets
	
	
	public SupplierRow(Integer id, String name, Integer contactDetailsId) {
		super(id);
		this.name = name;
		this.contactDetailsId = contactDetailsId;
	}
		
	public SupplierRow(@NonNull Supplier supplier) {
		super(supplier.getId());
		this.name = supplier.getName();
		this.phones = Arrays.stream(supplier.getContactDetails().getPhones())
				.map(Phone::getValue).collect(Collectors.toSet());
		this.emails = Arrays.stream(supplier.getContactDetails().getEmails())
				.map(Email::getValue).collect(Collectors.toSet());
		this.supplyCategories = supplier.getSupplyCategories().stream()
				.map(SupplyCategory::getValue).collect(Collectors.toSet());

	}
	
	public SupplierRow(Integer id, String name, 
			Set<String> phones, Set<String> emails, Set<String> supplyCategories) {
		super(id);
		this.name = name;
		this.phones = phones;
		this.emails = emails;
		this.supplyCategories = supplyCategories;
	}
	
}
