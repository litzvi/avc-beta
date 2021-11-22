/**
 * 
 */
package com.avc.mis.beta.dto.codes;

import java.util.Currency;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.enums.SupplyGroup;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductPoCodeDTO extends PoCodeDTO {

	public ProductPoCodeDTO(@NonNull Integer id, String code, Integer supplierId, Integer supplierVersion,
			String supplierName, Integer contractTypeId, String contractTypeValue, String contractTypeCode,
			Currency contractTypeCurrency, String contractTypeSuffix, SupplyGroup supplyGroup, Integer productCompanyId,
			Integer productCompanyVersion, String productCompanyName) {
		super(id, code, supplierId, supplierVersion, supplierName, contractTypeId, contractTypeValue, contractTypeCode,
				contractTypeCurrency, contractTypeSuffix, supplyGroup, productCompanyId, productCompanyVersion,
				productCompanyName);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProductPoCode.class;
	}
	
	@Override
	public ProductPoCode fillEntity(Object entity) {
		ProductPoCode poCodeEntity;
		if(entity instanceof ProductPoCode) {
			poCodeEntity = (ProductPoCode) entity;
		}
		else {
			throw new IllegalStateException("Param has to be PoCode class");
		}
		super.fillEntity(poCodeEntity);
		
		return poCodeEntity;
	}



}
