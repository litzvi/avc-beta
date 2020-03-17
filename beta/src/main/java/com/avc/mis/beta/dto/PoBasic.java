/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;

import com.avc.mis.beta.entities.process.PO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class PoBasic implements Serializable {

	@EqualsAndHashCode.Exclude
	private Integer id;
	private String contractTypeCode;
	private String supplierName;
	private String orderStatus;
	
	public PoBasic(PO po) {
		this.id = po.getId();
		this.contractTypeCode = po.getContractType().getValue();
		this.supplierName = po.getSupplier().getName();
		this.orderStatus = po.getStatus().toString();
	}
	
	public String getCode() {
		return this.contractTypeCode + this.id;
	}
}
