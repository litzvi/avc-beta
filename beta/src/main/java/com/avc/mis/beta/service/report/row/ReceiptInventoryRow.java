/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.stream.Stream;

import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ReceiptInventoryRow {

	String supplier;
	String item;
	String poCode;
	LocalDate receiptDate;
	String[] bags;
	AmountWithUnit amount;
	String[] warehouses;
	AmountWithCurrency unitPrice;
	Currency currency;
		
	public ReceiptInventoryRow(String supplier, 
			String item, 
			String poCode, OffsetDateTime receiptDate, String bags,
			BigDecimal amount, MeasureUnit measureUnit,
			String warehouses, 
			AmountWithCurrency unitPrice, Currency currency) {
		super();
		this.supplier = supplier;
		this.item = item;
		this.poCode = poCode;
		this.receiptDate = receiptDate.toLocalDate();
		if(bags != null) {
			this.bags = Stream.of(bags.split(",")).toArray(String[]::new);
		}
		else {
			this.bags = null;
		}
		this.amount = new AmountWithUnit(amount, measureUnit);
		this.amount.setScale(MeasureUnit.SCALE);
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
		this.unitPrice = unitPrice;
		this.currency = currency;
	} 
	
	public AmountWithUnit getWeightInLbs() {
		if(getAmount().getMeasureUnit() == MeasureUnit.LBS) {
			return getAmount();
		}
		
		try {
			return getAmount().convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE);
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

}
