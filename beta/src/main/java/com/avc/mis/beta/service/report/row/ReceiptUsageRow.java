/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.stream.Stream;

import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReceiptUsageRow extends ReceiptInventoryRow {

	private BigDecimal importedAmount;
	private BigDecimal usedAmount;
	private String[] usedDates;
	
	public ReceiptUsageRow(Integer poCodeId, String supplier, String productCompany, String item, boolean whole,
			String poCode, LocalDateTime receiptDate, ProductionFunctionality productionFunctionality, String bags,
			BigDecimal amount, MeasureUnit measureUnit, String warehouses, AmountWithCurrency unitPrice,
			Currency currency, ProcessStatus status, 
			BigDecimal importedAmount, BigDecimal usedAmount, String usedDates) {
		super(poCodeId, supplier, productCompany, item, whole, poCode, receiptDate, productionFunctionality, bags, amount,
				measureUnit, warehouses, unitPrice, currency, status);
		if(importedAmount != null)
			this.importedAmount = importedAmount.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		if(usedAmount != null)
			this.usedAmount = usedAmount.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		if(usedDates != null)
			this.usedDates = Stream.of(usedDates.split(",")).toArray(String[]::new);

	}
	
//	public ReceiptUsageRow(Integer poCodeId, String supplier, String productCompany, String item, boolean whole,
//			String poCode, LocalDateTime receiptDate, ProductionFunctionality productionFunctionality, String bags,
//			BigDecimal amount, MeasureUnit measureUnit, String warehouses, AmountWithCurrency unitPrice,
//			Currency currency, ProcessStatus status, 
//			BigDecimal importedAmount, BigDecimal usedAmount) {
//		this(poCodeId, supplier, productCompany, item, whole,
//				poCode, receiptDate, productionFunctionality, bags,
//				amount, measureUnit, warehouses, unitPrice,
//				currency, status, 
//				importedAmount, usedAmount, null);
//	}
	
	public void setUsedDates(String usedDates) {
		if(usedDates != null)
			this.usedDates = Stream.of(usedDates.split(",")).toArray(String[]::new);
	}
	
}
