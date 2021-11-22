/**
 * 
 */
package com.avc.mis.beta.entities.codes;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.ContractType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * PO code used for product orders (e.g. cashew)
 * 
 * @author zvi
 *
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Table(name = "ORDER_PO_CODES")
public class ProductPoCode extends BasePoCode {

	@NotNull(message = "Po code code is mandatory")
	@Override
	public String getCode() {
		return super.getCode();
	}
	
	@NotNull(message = "Po code supplier is mandatory")
	@Override
	public Supplier getSupplier() {
		return super.getSupplier();
	}
	
	@NotNull(message = "Po code contract type is mandatory ")
	@Override
	public ContractType getContractType() {
		return super.getContractType();
	}
	

}
