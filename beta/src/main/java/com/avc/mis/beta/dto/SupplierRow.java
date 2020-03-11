/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.entities.data.Email;
import com.avc.mis.beta.entities.data.Phone;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.data.SupplyCategory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class SupplierRow implements Serializable {
	@EqualsAndHashCode.Include
	private Integer id;
	private String name;
	private Set<String> phones;
	private Set<String> emails;
	private Set<String> supplyCategories;
	
	public SupplierRow(@NonNull Supplier supplier) {
		this.id = supplier.getId();
		this.name = supplier.getName();
		phones = Arrays.stream(supplier.getContactDetails().getPhones())
				.map(Phone::getValue).collect(Collectors.toSet());
		emails = Arrays.stream(supplier.getContactDetails().getEmails())
				.map(Email::getValue).collect(Collectors.toSet());
		supplyCategories = supplier.getSupplyCategories().stream()
				.map(SupplyCategory::getValue).collect(Collectors.toSet());

	}
}
