/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.Data;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Data
public class SupplierQualityRow {

	String supplier;
	String poCode;
	String receivedItem;
	LocalDateTime receiptDate;	
	AmountWithUnit received;
	
	BigDecimal humidity;
	BigDecimal breakage;
	BigDecimal rawDefects;
	BigDecimal rawDamage;
	BigDecimal roastDefects;
	BigDecimal roastDamage;
	
	AmountWithUnit inventory;
	AmountWithUnit loaded;

	AmountWithUnit badQuality;
	AmountWithUnit screen;
	
	public SupplierQualityRow() {
		
	}
	
	public BigDecimal getLoss() {	
		return AmountWithUnit.percentageLoss(AmountWithUnit.addNullable(getInventory(), getLoaded()), getReceived());
	}
	
	public BigDecimal getRawDefectsAndDamage() {
		return getRawDamage().add(getRawDefects());
	}
	
	public BigDecimal getRoastDefectsAndDamage() {
		return getRoastDamage().add(getRoastDefects());
	}

}
