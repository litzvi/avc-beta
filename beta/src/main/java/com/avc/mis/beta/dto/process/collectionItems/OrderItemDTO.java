/**
 * 
 */
package com.avc.mis.beta.dto.process.collectionItems;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.basic.ItemWithMeasureUnit;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collectionItems.OrderItem;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for OrderItem class, 
 * used to query database using select constructor (projection)
 * and for presenting relevant information to user.
 * Contains all fields of OrderItem class needed by user for creation or presentation.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrderItemDTO extends RankedAuditedDTO {

	private ItemWithMeasureUnit item;
	private AmountWithUnit numberUnits;
	private AmountWithCurrency unitPrice;
	private LocalDate deliveryDate;
	private String defects;
	private String remarks;
	private BigDecimal amountReceived;
	
	
	/**
	 * All arguments Constructor ,
	 * used to project directly from database without nested fetching.
	 */
	public OrderItemDTO(Integer id, Integer version, Integer ordinal, 
			Integer itemId, String itemValue, MeasureUnit itemMeasureUnit, 
			BigDecimal numberUnits, MeasureUnit measureUnit, BigDecimal unitPrice, Currency currency,
			LocalDate deliveryDate, String defects, String remarks, BigDecimal amountReceived) {
		super(id, version, ordinal);
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
	
	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public void setDeliveryDate(String deliveryDate) {
		if(deliveryDate != null)
			this.deliveryDate = LocalDate.parse(deliveryDate);
	}
	
	public AmountWithUnit getNumberUnits() {
		return this.numberUnits.setScale(MeasureUnit.SCALE);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return OrderItem.class;
	}
	
	@Override
	public OrderItem fillEntity(Object entity) {
		OrderItem orderItem;
		if(entity instanceof OrderItem) {
			orderItem = (OrderItem) entity;
		}
		else {
			throw new IllegalStateException("Param has to be OrderItem class");
		}
		super.fillEntity(orderItem);
		
		try {
			orderItem.setItem(getItem().fillEntity(new Item()));
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Item is mandatory");
		}
		orderItem.setNumberUnits(getNumberUnits());
		orderItem.setUnitPrice(getUnitPrice());
		orderItem.setDeliveryDate(getDeliveryDate());
		orderItem.setDefects(getDefects());
		orderItem.setRemarks(getRemarks());
		
		return orderItem;
	}
	
}
