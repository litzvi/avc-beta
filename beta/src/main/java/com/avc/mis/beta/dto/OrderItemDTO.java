/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.Optional;

import com.avc.mis.beta.entities.data.Item;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private Integer poId;
	private Item item;
	private BigDecimal numberUnits;
	private Currency currency;
	private BigDecimal unitPrice;
	private Calendar deliveryDate;
	private String defects;
	private String remarks;
	
	public OrderItemDTO(@NonNull OrderItem orderItem) {
		this.id = orderItem.getId();
		this.poId = orderItem.getPo().getId();
		this.item = orderItem.getItem();
		this.numberUnits = orderItem.getNumberUnits();
		this.currency = orderItem.getCurrency();
		this.unitPrice = orderItem.getUnitPrice();
		this.deliveryDate = orderItem.getDeliveryDate();
		this.defects = orderItem.getDefects();
		this.remarks = orderItem.getRemarks();
	}
	
	public String getCurrency() {
		return Optional.ofNullable(this.currency).map(c -> c.getCurrencyCode()).orElse(null);
	}
}
