/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.values.ItemWithMeasureUnit;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.avc.mis.beta.entities.process.collection.ReceiptItem;
import com.avc.mis.beta.entities.process.inventory.Storage;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * Data transfer object for OrderItem class, 
 * used to query database using select constructor (projection)
 * and for presenting relevant information to user.
 * Contains all fields of OrderItem class needed by user for creation or presentation.
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class OrderItemDTO extends SubjectDataDTO {

//	BasicValueEntity<Item> item;
	ItemWithMeasureUnit item;
	AmountWithUnit numberUnits;
	AmountWithCurrency unitPrice;
	LocalDate deliveryDate;
	String defects;
	String remarks;
	BigDecimal amountReceived;
	
	
	/**
	 * All arguments Constructor ,
	 * used to project directly from database without nested fetching.
	 */
	public OrderItemDTO(Integer id, Integer version, Integer ordinal, 
			Integer itemId, String itemValue, MeasureUnit itemMeasureUnit, 
			BigDecimal numberUnits, MeasureUnit measureUnit, BigDecimal unitPrice, Currency currency,
			LocalDate deliveryDate, String defects, String remarks, BigDecimal amountReceived) {
		super(id, version, ordinal);
//		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.item = new ItemWithMeasureUnit(itemId, itemValue, itemMeasureUnit);
		this.numberUnits = new AmountWithUnit(numberUnits.setScale(MeasureUnit.SCALE), measureUnit);
		if(unitPrice != null) {
			this.unitPrice = new AmountWithCurrency(unitPrice, currency);
		}
		else {
			this.unitPrice = null;
		}
		this.deliveryDate = deliveryDate;
		this.defects = defects;
		this.remarks = remarks;
		this.amountReceived = amountReceived;
	}
	
	/**
	 * Constructor from OrderItem object, used for testing.
	 * @param orderItem the OrderItem object
	 */
	public OrderItemDTO(@NonNull OrderItem orderItem) {
		super(orderItem.getId(), orderItem.getVersion(), orderItem.getOrdinal());
//		this.item = new BasicValueEntity<Item>(orderItem.getItem());
		this.item = new ItemWithMeasureUnit(orderItem.getItem());
		this.numberUnits = orderItem.getNumberUnits().setScale(MeasureUnit.SCALE);
		if(orderItem.getUnitPrice() != null) {
			this.unitPrice = orderItem.getUnitPrice().clone();
		}
		else {
			this.unitPrice = null;
		}
		this.deliveryDate = orderItem.getDeliveryDate();
		this.defects = orderItem.getDefects();
		this.remarks = orderItem.getRemarks();
		
		if(orderItem.getReceiptItems() != null) {
			AmountWithUnit sum = new AmountWithUnit(BigDecimal.ZERO, this.numberUnits.getMeasureUnit());
			for(ReceiptItem i: orderItem.getReceiptItems()) {
				MeasureUnit receiptMU = i.getMeasureUnit();
				if(i.getProcess().getLifeCycle().getProcessStatus() != ProcessStatus.CANCELLED) {
					for(Storage s: i.getStorageForms()) {
						sum.add(s.getUnitAmount().multiply(s.getNumberUnits()), receiptMU);
					}
				}
			}
			this.amountReceived = sum.getAmount();
		}
		else {
			this.amountReceived = null;		
		}
	}
	
}
