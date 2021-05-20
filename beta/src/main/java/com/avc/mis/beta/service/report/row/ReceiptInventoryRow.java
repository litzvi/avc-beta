/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.stream.Stream;

import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;

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
	ProcessStatus status;

		
	public ReceiptInventoryRow(String supplier, 
			String item, 
			String poCode, LocalDate receiptDate, String bags,
			BigDecimal amount, MeasureUnit measureUnit,
			String warehouses, 
			AmountWithCurrency unitPrice, Currency currency, ProcessStatus status) {
		super();
		this.supplier = supplier;
		this.item = item;
		this.poCode = poCode;
		this.receiptDate = receiptDate;
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
		this.status = status;
	} 
	
	public BigDecimal getWeightInLbs() {
		if(getAmount().getMeasureUnit() == MeasureUnit.LBS) {
			return getAmount().getAmount();
		}
		
		try {
			return getAmount().convert(MeasureUnit.LBS).getAmount().setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

}
