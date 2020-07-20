/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * DTO used for report/table to represent essential order-item information,
 * including PO information of the order, to be represented or aggregated in the table.
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoItemRow extends ValueDTO {
	
	String personInCharge;
	PoCodeBasic poCode;
	String supplierName;
	String itemName;
	AmountWithUnit numberUnits;
	OffsetDateTime contractDate;
	LocalDate deliveryDate;
	String defects;
	AmountWithCurrency unitPrice;
	BigDecimal receivedAmount;
	List<String> orderStatus;
	
	/**
	 * All arguments Constructor ,
	 * used to project directly from database without nested fetching.
	 */
	public PoItemRow(@NonNull Integer id, String personInCharge,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemName, BigDecimal amount, MeasureUnit measureUnit, 
			OffsetDateTime contractDate, LocalDate deliveryDate, 
			String defects, BigDecimal unitPrice, Currency currency, BigDecimal receivedAmount, long receiptsCancelled) {
		super(id);
		this.personInCharge = personInCharge;
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.numberUnits = new AmountWithUnit(amount.setScale(MeasureUnit.SCALE), measureUnit);
		this.contractDate = contractDate;
		this.deliveryDate = deliveryDate;
		this.defects = defects;
		this.unitPrice = new AmountWithCurrency(unitPrice, currency);
		this.receivedAmount = receivedAmount;
		
		this.orderStatus = new ArrayList<String>();
		
		if(receivedAmount == null) {
			this.orderStatus.add("OPEN");
		}
		else {
			switch(receivedAmount.compareTo(numberUnits.getAmount())) {
			case -1:
				this.orderStatus.add("OPEN");
				this.orderStatus.add("PARTLY RECEIVED");
				break;
			case 0:
			case 1:
				this.orderStatus.add("RECEIVED");
				break;
			}
		}
		
		if(receiptsCancelled > 0) {
			this.orderStatus.add("REJECTED");
		}
	}
		
	/**
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900002V
	 */
	public String getValue() {
		return this.poCode.getValue();
	}
		
}
