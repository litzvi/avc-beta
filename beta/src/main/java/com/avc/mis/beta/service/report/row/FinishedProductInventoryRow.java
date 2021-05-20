/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.stream.Stream;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class FinishedProductInventoryRow {

	String item;
	String[] poCodes;
	LocalDate[] receiptDates;
	LocalDate[] processDates;
	BigInteger units;
	AmountWithUnit amount;
	String[] warehouses;
	ProcessStatus status;
	
	
	public FinishedProductInventoryRow(String item, String poCodes, 
			String receiptDates, String processDates, 
			BigDecimal amount, MeasureUnit measureUnit,
			BigDecimal units, String warehouses, ProcessStatus status) {
		super();
		this.item = item;
		this.poCodes = Stream.of(poCodes.split(",")).toArray(String[]::new);
		this.receiptDates = Stream.of(receiptDates.split(",")).map(j -> LocalDate.parse(j)).toArray(LocalDate[]::new);
		this.processDates = Stream.of(processDates.split(",")).map(j -> LocalDate.parse(j)).toArray(LocalDate[]::new);
		this.units = units.setScale(0, RoundingMode.HALF_DOWN).toBigInteger();
		this.amount = new AmountWithUnit(amount, measureUnit);
		this.amount.setScale(MeasureUnit.SCALE);
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}	
		this.status = status;
	}


	public AmountWithUnit getWeightInLbs() {
		//TODO
		return null;
	}
}
