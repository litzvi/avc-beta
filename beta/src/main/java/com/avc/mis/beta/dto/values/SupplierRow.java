/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.data.Email;
import com.avc.mis.beta.entities.data.Phone;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.SupplyCategory;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class SupplierRow extends ValueDTO {
	
	String name;
	Set<String> phones;
	Set<String> emails;
	Set<String> supplyCategories;
		
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
}
