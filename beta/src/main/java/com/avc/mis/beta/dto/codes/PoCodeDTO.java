/**
 * 
 */
package com.avc.mis.beta.dto.codes;

import java.util.Currency;

import com.avc.mis.beta.dto.BaseEntityDTO;
import com.avc.mis.beta.dto.basic.DataObjectWithName;
import com.avc.mis.beta.dto.values.ContractTypeDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.ValueInterface;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.values.ContractType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO for po code
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoCodeDTO extends BaseEntityDTO implements ValueInterface {

	private String code;
	private DataObjectWithName<Supplier> supplier; 
	private ContractTypeDTO contractType;
	private DataObjectWithName<Supplier> productCompany; 

	
	public PoCodeDTO(@NonNull Integer id, String code, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			Integer contractTypeId, String contractTypeValue, String contractTypeCode, 
			Currency contractTypeCurrency, String contractTypeSuffix, SupplyGroup supplyGroup, 
			Integer productCompanyId, Integer productCompanyVersion, String productCompanyName) {
		super(id);
		this.code = code;
		this.supplier = new DataObjectWithName<Supplier>(supplierId, supplierVersion, supplierName);
		this.contractType = new ContractTypeDTO(contractTypeId, contractTypeValue, contractTypeCode, 
				contractTypeCurrency, contractTypeSuffix, supplyGroup);
		if(productCompanyId != null)
			this.productCompany = new DataObjectWithName<Supplier>(productCompanyId, productCompanyVersion, productCompanyName);
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900001V
	 */
	public String getValue() {
		return String.format("%s-%s%s", this.contractType.getCode(), this.code, this.contractType.getSuffix());
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return BasePoCode.class;
	}
	
	@Override
	public BasePoCode fillEntity(Object entity) {
		BasePoCode poCodeEntity;
		if(entity instanceof BasePoCode) {
			poCodeEntity = (BasePoCode) entity;
		}
		else {
			throw new IllegalStateException("Param has to be BasePoCode class");
		}
		super.fillEntity(poCodeEntity);
		poCodeEntity.setCode(getCode());
		if(getSupplier() == null) {
			throw new IllegalArgumentException("Po Code has to reference a supplier");
		}
		else {
			poCodeEntity.setSupplier((Supplier) getSupplier().fillEntity(new Supplier()));
		}
		if(getContractType() == null) {
			throw new IllegalArgumentException("Contract Type is mandatory");
		}
		else {
			poCodeEntity.setContractType((ContractType) getContractType().fillEntity(new ContractType()));
		}
		if(getProductCompany() != null) {
			poCodeEntity.setProductCompany((Supplier) getProductCompany().fillEntity(new Supplier()));;
		}
		
		return poCodeEntity;
	}
}
