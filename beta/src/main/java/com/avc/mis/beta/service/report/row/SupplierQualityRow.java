/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class SupplierQualityRow extends BasicDTO {

	String poCode;
	String supplier;
	String receivedItem;
	LocalDateTime receiptTime;
	AmountWithUnit received;
	
	BigDecimal humidity;
	BigDecimal breakage;
	BigDecimal rawDefects;
	BigDecimal rawDamage;
	BigDecimal roastDefects;
	BigDecimal roastDamage;
	
	//get a list by item including QC and Screen
	AmountWithUnit currentAmount;//including loaded

	AmountWithUnit badQuality;
	AmountWithUnit screen;
	
	public SupplierQualityRow(Integer poCodeId, String poCode, String supplier, 
			String receivedItem, LocalDateTime receiptTime, 
			BigDecimal receivedAmount, MeasureUnit receivedMeasureUnit) {
		super(poCodeId);
		this.poCode = poCode;
		this.supplier = supplier;
		this.receivedItem = receivedItem;
		this.receiptTime = receiptTime;
		this.received = new AmountWithUnit(receivedAmount, receivedMeasureUnit);
	}
	
	public BigDecimal getLoss() {	
		return AmountWithUnit.percentageLoss(getCurrentAmount(), getReceived());
	}
	
	public BigDecimal getRawDefectsAndDamage() {
		return getRawDamage().add(getRawDefects());
	}
	
	public BigDecimal getRoastDefectsAndDamage() {
		return getRoastDamage().add(getRoastDefects());
	}

}
