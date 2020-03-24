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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
public class SupplierRow extends ValueDTO {
	
	private String name;
	private Set<String> phones;
	private Set<String> emails;
	private Set<String> supplyCategories;
		
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
