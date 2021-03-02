/**
 * 
 */
package com.avc.mis.beta.entities.codes;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.UniqueElements;

import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.ContractType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Table(name = "ORDER_PO_CODES")
//@PrimaryKeyJoinColumn(name = "poCodeId")
//@DiscriminatorValue("po_code")
public class PoCode extends BasePoCode {

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
	
//	@Null(message = "Po code doesn't have display ")
//	@Override
//	public String getDisplay() {
//		return super.getDisplay();
//	}


}
