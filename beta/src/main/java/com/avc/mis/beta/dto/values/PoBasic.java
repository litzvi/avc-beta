/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.io.Serializable;

import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.PO;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
public class PoBasic implements Serializable {

	@EqualsAndHashCode.Exclude
	Integer id;
	String contractTypeCode;
	String supplierName;
	OrderStatus orderStatus;
	
	@lombok.experimental.Tolerate
	public PoBasic(PO po) {
		this.id = po.getId();
		this.contractTypeCode = po.getContractType().getValue();
		this.supplierName = po.getSupplier().getName();
		this.orderStatus = po.getStatus();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return this.contractTypeCode + this.id;
	}
	
	public String getOrderStatus() {
		return this.orderStatus.toString();
	}
}
