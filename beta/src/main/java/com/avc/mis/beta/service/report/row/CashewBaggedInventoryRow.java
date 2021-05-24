/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.CashewGrade;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SaltLevel;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.Getter;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class CashewBaggedInventoryRow {

	ItemWithUnitDTO item;
	String brand;
	String code;
	boolean whole;
	CashewGrade grade;
	AmountWithUnit bagSize;
	SaltLevel saltLevel;
	int bagsInBox;
	AmountWithUnit totalAmount;//amount of boxes
	
	public CashewBaggedInventoryRow(
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz,
			String brand, String code, 
			boolean whole, CashewGrade grade, SaltLevel saltLevel, int numBags, 
			BigDecimal amount, MeasureUnit measureUnit) {
		super();
		this.item = new ItemWithUnitDTO(itemId, itemValue, defaultMeasureUnit, itemGroup, productionUse, unitAmount, unitMeasureUnit, clazz);
		this.brand = brand;
		this.code = code;
		this.whole = whole;
		this.grade = grade;
		this.saltLevel = saltLevel;
		this.bagsInBox = numBags;
		this.bagSize = this.item.getUnit().divide(BigDecimal.valueOf(numBags));
		this.totalAmount = new AmountWithUnit(amount, measureUnit);
	}
	
	public BigDecimal getBagQuantity() {
		if(getTotalAmount() != null && MeasureUnit.DISCRETE_UNITS.contains(getTotalAmount().getMeasureUnit()) && getBagsInBox() > 1) {
			return getTotalAmount().getAmount().multiply(BigDecimal.valueOf(getBagsInBox()));
		}
		else {
			return null;
		}
	}
	
	public BigDecimal getBoxQuantity() {
		if(getTotalAmount() != null && MeasureUnit.DISCRETE_UNITS.contains(getTotalAmount().getMeasureUnit())) {
			return getTotalAmount().setScale(MeasureUnit.SCALE).getAmount();
		}
		else {
			return null;
		}
	}
	
	public BigDecimal getWeightInLbs() {
		if(getTotalAmount() == null) {
			return null;
		}
		AmountWithUnit weight;
		if(MeasureUnit.NONE == getItem().getUnit().getMeasureUnit() && MeasureUnit.WEIGHT_UNITS.contains(getTotalAmount().getMeasureUnit())) {
			weight = getTotalAmount();
		}
		else if(MeasureUnit.WEIGHT_UNITS.contains(getItem().getUnit().getMeasureUnit())) {
			weight = new AmountWithUnit(
					getTotalAmount().getAmount()
					.multiply(getItem().getUnit().getAmount(), MathContext.DECIMAL64), 
					getItem().getUnit().getMeasureUnit());
		}
		else {
			return null;			
		}
		
		if(weight.getMeasureUnit() == MeasureUnit.LBS) {
			return weight.setScale(MeasureUnit.SCALE).getAmount();
		}
		
		return weight.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE).getAmount();
	}
	
	public String getType() {
		if(isWhole()) {
			return "WHOLE";
		}
		else {
			return "H&P";
		}
	}
	
	

}
