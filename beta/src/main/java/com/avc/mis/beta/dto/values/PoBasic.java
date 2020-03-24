/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.PO;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class PoBasic extends ValueDTO {

	String contractTypeCode;
	String supplierName;
	OrderStatus orderStatus;
	
	/**
	 * @param id
	 * @param contractTypeCode
	 * @param supplierName
	 * @param orderStatus
	 */
	public PoBasic(@NonNull Integer id, String contractTypeCode, String supplierName, OrderStatus orderStatus) {
		super(id);
		this.contractTypeCode = contractTypeCode;
		this.supplierName = supplierName;
		this.orderStatus = orderStatus;
	}
	
	public PoBasic(PO po) {
		super(po.getId());
		this.contractTypeCode = po.getContractType().getValue();
		this.supplierName = po.getSupplier().getName();
		this.orderStatus = po.getStatus();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return this.contractTypeCode + this.getId();
	}
	
	public String getOrderStatus() {
		return this.orderStatus.toString();
	}

	
}
