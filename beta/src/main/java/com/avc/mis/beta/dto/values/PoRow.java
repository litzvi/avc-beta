/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.OrderItem;
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
public class PoRow extends ValueDTO {
	
	String contractTypeCode;
	String supplierName;
	String itemName;
	BigDecimal amount;
	MeasureUnit measureUnit;
	LocalDateTime contractDate;
	LocalDate deliveryDate;
	OrderStatus orderStatus;
	
	/**
	 * @param id
	 * @param contractTypeCode
	 * @param supplierName
	 * @param itemName
	 * @param amount
	 * @param measureUnit
	 * @param contractDate
	 * @param deliveryDate
	 * @param orderStatus
	 */
	public PoRow(@NonNull Integer id, String contractTypeCode, String supplierName, String itemName, BigDecimal amount,
			MeasureUnit measureUnit, LocalDateTime contractDate, LocalDate deliveryDate, OrderStatus orderStatus) {
		super(id);
		this.contractTypeCode = contractTypeCode;
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.amount = amount;
		this.measureUnit = measureUnit;
		this.contractDate = contractDate;
		this.deliveryDate = deliveryDate;
		this.orderStatus = orderStatus;
	}
	
	/**
	 * @param po
	 * @param orderItem
	 */
	public PoRow(PO po, OrderItem orderItem) {

		super(po.getId());
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
		return this.contractTypeCode + this.getId();
	}
	
	public String getMeasureUnit() {
		return this.measureUnit.toString();
	}
	
	public String getOrderStatus() {
		return this.orderStatus.toString();
	}

	
}
