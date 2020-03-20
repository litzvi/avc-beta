/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
public class PoRow implements Serializable {
	
	@EqualsAndHashCode.Exclude
	Integer id;
	String contractTypeCode;
	String supplierName;
	String itemName;
	BigDecimal amount;
	MeasureUnit measureUnit;
	LocalDateTime contractDate;
	LocalDate deliveryDate;
	OrderStatus orderStatus;
	
	/**
	 * @param po
	 * @param orderItem
	 */
	@lombok.experimental.Tolerate
	public PoRow(PO po, OrderItem orderItem) {

		this.id = po.getId();
		this.contractTypeCode = po.getContractType().getValue();
		this.supplierName = po.getSupplier().getName();
		this.itemName = orderItem.getItem().getValue();
		this.amount = orderItem.getNumberUnits();
		this.measureUnit = orderItem.getMeasureUnit();
		this.contractDate = po.getOrderProcess().getTime();
		this.deliveryDate = orderItem.getDeliveryDate();
		this.orderStatus = po.getStatus();
		
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return this.contractTypeCode + this.id;
	}
	
	public String getMeasureUnit() {
		return this.measureUnit.toString();
	}
	
	public String getOrderStatus() {
		return this.orderStatus.toString();
	}
}
