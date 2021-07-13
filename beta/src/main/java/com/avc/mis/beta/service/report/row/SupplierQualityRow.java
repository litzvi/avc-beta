/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringExclude;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ItemAmountWithPo;
import com.avc.mis.beta.dto.report.ItemQc;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierQualityRow extends BasicDTO {

	private String poCode;
	private String supplier;
	private String receivedItem;
	private LocalDateTime receiptTime;
	private AmountWithUnit received;
	
	private BigDecimal humidity;
	private BigDecimal breakage;
	private BigDecimal rawDefects;
	private BigDecimal rawDamage;
	private BigDecimal roastDefects;
	private BigDecimal roastDamage;
	
	//get a list by item including QC and Screen
	@ToString.Exclude private List<ItemAmountWithPo> outAmounts;//including loaded
	private AmountWithUnit totalOutAmount;
	
	public SupplierQualityRow(Integer poCodeId, String poCode, String supplier, 
			String receivedItem, LocalDateTime receiptTime, 
			BigDecimal receivedAmount, MeasureUnit receivedMeasureUnit) {
		super(poCodeId);
		this.poCode = poCode;
		this.supplier = supplier;
		this.receivedItem = receivedItem;
		this.receiptTime = receiptTime;
		this.received = new AmountWithUnit(receivedAmount, receivedMeasureUnit).setScale(MeasureUnit.SCALE);
	}
	
	public void setOutAmounts(List<ItemAmountWithPo> outAmounts) {
		boolean empty = outAmounts == null || outAmounts.isEmpty();
		this.outAmounts = empty ? null : outAmounts;
		this.totalOutAmount = empty ? null : ItemAmount.getTotalWeight(outAmounts);
	}
	
	
	public void setItemQcs(List<ItemQc> itemQcs) {
		if(itemQcs == null) {
			return;
		}
		ItemQc raw = null;
		ItemQc roast = null;
		for(int i=0; i < itemQcs.size(); i++) {
			if(raw == null && itemQcs.get(i).getItemProductionUse() == ProductionUse.RAW_KERNEL) {
				raw = itemQcs.get(i);
			}
			if(roast == null && itemQcs.get(i).getItemProductionUse() == ProductionUse.ROAST) {
				roast = itemQcs.get(i);
			}
		}
		if(raw != null) {
			this.humidity = raw.getHumidity();
			this.breakage = raw.getBreakage();
			this.rawDefects = raw.getTotalDefects();
			this.rawDamage = raw.getTotalDamage();
		}
		if(roast != null) {
			this.roastDefects = roast.getTotalDefects();
			this.roastDamage = roast.getTotalDamage();
		}
		
	}
	
	public AmountWithUnit getBadQuality() {
		if(this.outAmounts != null) {
			return this.outAmounts.stream()
					.filter(i -> i.getItemGroup() == ItemGroup.QC)
					.map(i -> i.getWeightAmount()).reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_LBS);
		}
		else {
			return null;			
		}
	}
	
	public AmountWithUnit getWaste() {
		if(this.outAmounts != null) {
			return this.outAmounts.stream()
					.filter(i -> i.getItemGroup() == ItemGroup.WASTE)
					.map(i -> i.getWeightAmount()).reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_LBS);
		}
		else {
			return null;			
		}
	}
	
	public BigDecimal getLoss() {	
		return AmountWithUnit.percentageLoss(getTotalOutAmount(), getReceived());
	}
	
	public BigDecimal getRawDefectsAndDamage() {
		if(this.rawDamage != null && this.rawDefects != null) {
			return this.rawDamage.add(this.rawDefects);
		}
		else if(this.rawDamage != null) {
			return this.rawDamage;
		}
		else if(this.rawDefects != null) {
			return this.rawDefects;
		}
		return null;
	}
	
	public BigDecimal getRoastDefectsAndDamage() {
		if(this.roastDamage != null && this.roastDefects != null) {
			return this.roastDamage.add(this.roastDefects);
		}
		else if(this.roastDamage != null) {
			return this.roastDamage;
		}
		else if(this.roastDefects != null) {
			return this.roastDefects;
		}
		return null;
	}

	

}
