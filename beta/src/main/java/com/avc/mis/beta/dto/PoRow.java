/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.avc.mis.beta.entities.process.OrderItem;
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
public class PoRow implements Serializable {
	
	@EqualsAndHashCode.Exclude
	private Integer id;
	private String contractTypeCode;
	private String supplierName;
	private String itemName;
	private BigDecimal amount;
	private String measureUnit;
	private Date contractDate;
	private Calendar deliveryDate;
	private String orderStatus;
	
	/**
	 * @param po
	 * @param orderItem
	 */
	public PoRow(PO po, OrderItem orderItem) {

		this.id = po.getId();
		this.contractTypeCode = po.getContractType().getValue();
		this.supplierName = po.getSupplier().getName();
		this.itemName = orderItem.getItem().getValue();
		this.amount = orderItem.getNumberUnits();
		this.measureUnit = orderItem.getMeasureUnit();
		this.contractDate = po.getOrderProcess().getTime();
		this.deliveryDate = orderItem.getDeliveryDate();
		this.orderStatus = po.getStatus().toString();
		
	}
	
	public String getCode() {
		return this.contractTypeCode + this.id;
	}
}
