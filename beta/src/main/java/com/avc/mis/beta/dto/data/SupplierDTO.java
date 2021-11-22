/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.values.SupplyCategoryDTO;
import com.avc.mis.beta.entities.BaseEntity;
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
	
	private Set<SupplyCategoryDTO> supplyCategories = new HashSet<>();
	
	public SupplierDTO(@NonNull Supplier supplier, boolean hasContacts) {
		super(supplier, hasContacts);
		this.supplyCategories.addAll(supplier.getSupplyCategories().stream().map(i -> new SupplyCategoryDTO(i)).collect(Collectors.toSet()));
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Supplier.class;
	}
	
	@Override
	public Supplier fillEntity(Object entity) {
		Supplier companyEntity;
		if(entity instanceof Supplier) {
			companyEntity = (Supplier) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Supplier class");
		}
		super.fillEntity(companyEntity);
		
		if(getSupplyCategories() != null) {
			companyEntity.setSupplyCategories(getSupplyCategories().stream().map(i -> i.fillEntity(new SupplyCategory())).collect(Collectors.toSet()));
		}
		
		return companyEntity;
	}
	
}
