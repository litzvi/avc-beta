/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.values.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class OrderItemDTO extends ProcessDTO {

//	@EqualsAndHashCode.Exclude // for testing 
//	private Integer poId; //perhaps not needed, and if yes maybe get the whole PoCode
	BasicValueEntity<Item> item;
	AmountWithUnit numberUnits;
//	MeasureUnit measureUnit;
//	BigDecimal numberUnits;
	AmountWithCurrency unitPrice;
//	Currency currency;
//	BigDecimal unitPrice;
	LocalDate deliveryDate;
	String defects;
	String remarks;
	Boolean received;
	
	
	public OrderItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			BigDecimal numberUnits, MeasureUnit measureUnit, BigDecimal unitPrice, Currency currency,
			LocalDate deliveryDate, String defects, String remarks, Boolean received) {
		super(id, version);
//		this.poId = poId;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
//		this.measureUnit = measureUnit;
		this.numberUnits = new AmountWithUnit(numberUnits.setScale(AmountWithUnit.SCALE), measureUnit);
//		this.currency = currency;
		this.unitPrice = new AmountWithCurrency(unitPrice, currency);
		this.deliveryDate = deliveryDate;
		this.defects = defects;
		this.remarks = remarks;
		this.received = received;

//		this.numberUnits.setScale(3);//for testing with assertEquals
//		this.unitPrice.setScale(2);//for testing with assertEquals
	}
	
	public OrderItemDTO(@NonNull OrderItem orderItem) {
		super(orderItem.getId(), orderItem.getVersion());
//		this.poId = orderItem.getPo().getId();
		this.item = new BasicValueEntity<Item>(orderItem.getItem());
//		this.measureUnit = orderItem.getMeasureUnit();
		this.numberUnits = orderItem.getNumberUnits().setScale(AmountWithUnit.SCALE);
//		this.currency = orderItem.getCurrency();
		this.unitPrice = orderItem.getUnitPrice().clone();
		this.deliveryDate = orderItem.getDeliveryDate();
		this.defects = orderItem.getDefects();
		this.remarks = orderItem.getRemarks();
		this.received = false; //should be null - set as false for testing for a newly created order
		
//		this.numberUnits.setScale(3);//for testing with assertEquals
//		this.unitPrice.setScale(2);//for testing with assertEquals
	}
	
//	public String getCurrency() {
//		return Optional.ofNullable(this.currency).map(c -> c.getCurrencyCode()).orElse(null);
//	}
}
