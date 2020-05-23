/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.ValueObject;
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
	ValueObject item;
	MeasureUnit measureUnit;
	BigDecimal numberUnits;
	Currency currency;
	BigDecimal unitPrice;
	LocalDate deliveryDate;
	String defects;
	String remarks;
	Boolean received;
	
	
	public OrderItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			MeasureUnit measureUnit, BigDecimal numberUnits, Currency currency, BigDecimal unitPrice,
			LocalDate deliveryDate, String defects, String remarks, Boolean received) {
		super(id, version);
//		this.poId = poId;
		this.item = new ValueObject(itemId, itemValue);
		this.measureUnit = measureUnit;
		this.numberUnits = numberUnits.setScale(3);
		this.currency = currency;
		this.unitPrice = unitPrice.setScale(2);
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
		this.item = new ValueObject(orderItem.getItem());
		this.measureUnit = orderItem.getMeasureUnit();
		this.numberUnits = orderItem.getNumberUnits().setScale(3);
		this.currency = orderItem.getCurrency();
		this.unitPrice = orderItem.getUnitPrice().setScale(2);
		this.deliveryDate = orderItem.getDeliveryDate();
		this.defects = orderItem.getDefects();
		this.remarks = orderItem.getRemarks();
		this.received = false; //should be null - set as false for testing for a newly created order
		
//		this.numberUnits.setScale(3);//for testing with assertEquals
//		this.unitPrice.setScale(2);//for testing with assertEquals
	}
	
	public String getCurrency() {
		return Optional.ofNullable(this.currency).map(c -> c.getCurrencyCode()).orElse(null);
	}
}
