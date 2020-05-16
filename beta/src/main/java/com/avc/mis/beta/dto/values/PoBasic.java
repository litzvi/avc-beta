/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.DataDTO;
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
public class PoBasic extends DataDTO {

	Integer poCode;
	String value;
	String supplierName;
	SupplierBasic supplier;
	OrderStatus orderStatus;
	
	public PoBasic(@NonNull Integer id, Integer version, PoCode poCode, 
			String supplierName, Integer supplierId, Integer supplierVersion, OrderStatus orderStatus) {
		super(id, version);
		this.poCode  = poCode.getCode();
		this.value = poCode.getValue();
		this.supplierName = supplierName;
		this.supplier = new SupplierBasic(supplierId, supplierVersion, supplierName);
		this.orderStatus = orderStatus;
	}
	
	public PoBasic(PO po) {
		super(po.getId(), po.getVersion());
		this.poCode  = po.getPoCode().getCode();
		this.value = po.getPoCode().getValue();
		this.supplierName = po.getSupplier().getName();
		this.supplier = new SupplierBasic(
				po.getSupplier().getId(), po.getSupplier().getVersion(), po.getSupplier().getName());
		this.orderStatus = po.getOrderStatus();
	}
	
		
	public String getOrderStatus() {
		return this.orderStatus.toString();
	}

	
}
