/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SaltLevel;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.values.CashewGrade;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class CashewBaggedInventoryRow {

	private ItemWithUnitDTO item;
	private String brand;
	private String code;
	private boolean whole;
	private boolean roast;
	private boolean toffee;
	private BasicValueEntity<CashewGrade> grade;
	private AmountWithUnit bagSize;
	private SaltLevel saltLevel;
	private int bagsInBox;
	private AmountWithUnit totalAmount;//amount of boxes
	private BigDecimal weightCoefficient;
	
	private BigDecimal boxQuantity;
	
	public CashewBaggedInventoryRow(
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz,
			String brand, String code, 
			boolean whole, boolean roast, boolean toffee,
			Integer gradeId,  String gradeValue,
			SaltLevel saltLevel, int numBags, 
			BigDecimal amount, MeasureUnit measureUnit, BigDecimal boxQuantity) {
		this(itemId, itemValue, defaultMeasureUnit, 
				itemGroup, productionUse, 
				unit, clazz, 
				brand, code, 
				whole, roast, toffee,
				gradeId, gradeValue, 
				saltLevel, numBags, 
				amount, measureUnit, boxQuantity, BigDecimal.ONE);
	}
	
	public CashewBaggedInventoryRow(
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz,
			String brand, String code, 
			boolean whole, boolean roast, boolean toffee,
			Integer gradeId,  String gradeValue,
			SaltLevel saltLevel, int numBags, 
			BigDecimal amount, MeasureUnit measureUnit, BigDecimal boxQuantity, BigDecimal weightCoefficient) {
		super();
		this.item = new ItemWithUnitDTO(itemId, itemValue, defaultMeasureUnit, itemGroup, productionUse, unit, clazz);
		this.brand = brand;
		this.code = code;
		this.whole = whole;
		this.roast = roast;
		this.toffee = toffee;
		if(gradeId != null && gradeValue != null)
			this.grade = new BasicValueEntity<CashewGrade>(gradeId, gradeValue);
		else
			this.grade = null;
		this.saltLevel = saltLevel;
		this.bagsInBox = numBags;
		this.bagSize = this.item.getUnit().divide(BigDecimal.valueOf(numBags));
		this.totalAmount = new AmountWithUnit(amount, measureUnit);
		this.boxQuantity = boxQuantity;
		this.weightCoefficient = weightCoefficient;
		
	}
	
	public CashewBaggedInventoryRow(ItemWithUnitDTO item, String brand, String code, boolean whole, boolean roast,
			boolean toffee, BasicValueEntity<CashewGrade> grade, AmountWithUnit bagSize, SaltLevel saltLevel,
			int bagsInBox, AmountWithUnit totalAmount, BigDecimal boxQuantity, BigDecimal weightCoefficient) {
		super();
		this.item = item;
		this.brand = brand;
		this.code = code;
		this.whole = whole;
		this.roast = roast;
		this.toffee = toffee;
		this.grade = grade;
		this.bagSize = bagSize;
		this.saltLevel = saltLevel;
		this.bagsInBox = bagsInBox;
		this.totalAmount = totalAmount;
		this.boxQuantity = boxQuantity;
		this.weightCoefficient = weightCoefficient;
	}
	
	public CashewBaggedInventoryRow(CashewBaggedInventoryRow row) {
		super();
		this.item = row.getItem();
		this.brand = row.getBrand();
		this.code = row.getCode();
		this.whole = row.isWhole();
		this.roast = row.isRoast();
		this.toffee = row.isToffee();
		this.grade = row.getGrade();
		this.saltLevel = row.getSaltLevel();
		this.bagsInBox = row.getBagsInBox();
		this.bagSize = row.getBagSize();
		this.totalAmount = row.getTotalAmount();
		this.boxQuantity = row.getBoxQuantity();
		this.weightCoefficient = row.getWeightCoefficient();
	}

	public BigDecimal getBagQuantity() {
		if(getTotalAmount() != null && MeasureUnit.DISCRETE_UNITS.contains(getTotalAmount().getMeasureUnit()) && getBagsInBox() > 1) {
			return getTotalAmount()
					.getAmount()
					.multiply(getWeightCoefficient(), MathContext.DECIMAL64)
					.multiply(BigDecimal.valueOf(getBagsInBox()))
					.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		}
		else {
			return null;
		}
	}
	
//	public BigDecimal getBoxQuantity() {
//		if(getTotalAmount() != null && MeasureUnit.DISCRETE_UNITS.contains(getTotalAmount().getMeasureUnit())) {
//			return getTotalAmount()
//					.getAmount()
//					.multiply(getWeightCoefficient(), MathContext.DECIMAL64)
//					.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
//		}
//		else {
//			return null;
//		}
//	}
	
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
		
		if(weight.getMeasureUnit() != MeasureUnit.LBS) {
			weight = weight.convert(MeasureUnit.LBS);
		}
				
		return weight.getAmount()
				.multiply(getWeightCoefficient(), MathContext.DECIMAL64)
				.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
	}
	
	public AmountWithUnit getBoxWeight() {
		if(MeasureUnit.NONE == getItem().getUnit().getMeasureUnit()){ 
			return null;
		}
		else {
			return getItem().getUnit();
		}
	}
	
	public String getType() {
		if(isWhole()) {
			return "WHOLE";
		}
		else {
			return "H&P";
		}
	}
	
	public ItemGroup getItemGroup() {
		return this.item.getGroup();
	}

	

}
