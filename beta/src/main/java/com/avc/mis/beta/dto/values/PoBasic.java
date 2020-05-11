/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class PoBasic extends ValueDTO {

	Integer poCode;
	String value;
	String supplierName;
	OrderStatus orderStatus;
	
	public PoBasic(@NonNull Integer id, PoCode poCode, String supplierName, OrderStatus orderStatus) {
		super(id);
		this.poCode  = poCode.getCode();
		this.value = poCode.getValue();
		this.supplierName = supplierName;
		this.orderStatus = orderStatus;
	}
	
	public PoBasic(PO po) {
		super(po.getId());
		this.poCode  = po.getPoCode().getCode();
		this.value = po.getPoCode().getValue();
		this.supplierName = po.getSupplier().getName();
		this.orderStatus = po.getOrderStatus();
	}
	
		
	public String getOrderStatus() {
		return this.orderStatus.toString();
	}

	
}
