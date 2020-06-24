/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Currency;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.processinfo.OrderItem;

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
@ToString(callSuper = true)
public class PoRow extends ValueDTO {
	
	PoCodeBasic poCode;
	String supplierName;
	String itemName;
	AmountWithUnit numberUnits;
//	MeasureUnit measureUnit;
	OffsetDateTime contractDate;
	LocalDate deliveryDate;
//	OrderStatus orderStatus;
	String defects;
	AmountWithCurrency unitPrice;
//	Currency currency;
//	BigDecimal unitPrice;
	
	public PoRow(@NonNull Integer id, 
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, 
			String itemName, BigDecimal amount,
			MeasureUnit measureUnit, OffsetDateTime contractDate, LocalDate deliveryDate, 
			String defects, BigDecimal unitPrice, Currency currency) {
		super(id);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode);
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.numberUnits = new AmountWithUnit(amount.setScale(AmountWithUnit.SCALE), measureUnit);
//		this.measureUnit = measureUnit;
		this.contractDate = contractDate;
		this.deliveryDate = deliveryDate;
//		this.orderStatus = orderStatus;
		this.defects = defects;
//		this.currency = currency;
		this.unitPrice = new AmountWithCurrency(unitPrice, currency);
	
	}
	
	/**
	 * @param po
	 * @param orderItem
	 */
	public PoRow(PO po, OrderItem orderItem) {

		super(po.getId());
		PoCode poCode = po.getPoCode();
		this.poCode = new PoCodeBasic(poCode);
		this.supplierName = poCode.getSupplier().getName();
		this.itemName = orderItem.getItem().getValue();
		this.numberUnits = orderItem.getNumberUnits().clone();
//		this.measureUnit = orderItem.getMeasureUnit();
		this.contractDate = po.getRecordedTime();
		this.deliveryDate = orderItem.getDeliveryDate();
//		this.orderStatus = po.getOrderStatus();
		this.defects = orderItem.getDefects();
//		this.currency = orderItem.getCurrency();
		this.unitPrice = orderItem.getUnitPrice().clone();
		
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001
	 */
	public String getValue() {
		return this.poCode.getValue();
	}
	
//	public String getMeasureUnit() {
//		return this.measureUnit.toString();
//	}
//	
//	public String getOrderStatus() {
//		return this.orderStatus.toString();
//	}

	
}
