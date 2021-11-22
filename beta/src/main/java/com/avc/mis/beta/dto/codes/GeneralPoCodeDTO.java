/**
 * 
 */
package com.avc.mis.beta.dto.codes;

import java.util.Currency;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
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
public class GeneralPoCodeDTO extends PoCodeDTO {

	public GeneralPoCodeDTO(@NonNull Integer id, String code, Integer supplierId, Integer supplierVersion,
			String supplierName, Integer contractTypeId, String contractTypeValue, String contractTypeCode,
			Currency contractTypeCurrency, String contractTypeSuffix, SupplyGroup supplyGroup, Integer productCompanyId,
			Integer productCompanyVersion, String productCompanyName) {
		super(id, code, supplierId, supplierVersion, supplierName, contractTypeId, contractTypeValue, contractTypeCode,
				contractTypeCurrency, contractTypeSuffix, supplyGroup, productCompanyId, productCompanyVersion,
				productCompanyName);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return GeneralPoCode.class;
	}

	@Override
	public GeneralPoCode fillEntity(Object entity) {
		GeneralPoCode poCodeEntity;
		if(entity instanceof GeneralPoCode) {
			poCodeEntity = (GeneralPoCode) entity;
		}
		else {
			throw new IllegalStateException("Param has to be GeneralPoCode class");
		}
		super.fillEntity(poCodeEntity);
		
		return poCodeEntity;
	}
}
