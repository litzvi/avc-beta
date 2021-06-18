/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.CashewGrade;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SaltLevel;
import com.avc.mis.beta.entities.item.CashewItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class CashewItemDTO extends ItemDTO {

	int numBags;
	CashewGrade grade;
	boolean whole;
	boolean roast;
	boolean toffee;
	SaltLevel saltLevel;
	
	
	
	public CashewItemDTO(Integer id, String value, 
			String code, String brand, MeasureUnit measureUnit, ItemGroup group, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz, 
			int numBags, CashewGrade grade, boolean whole, boolean roast, boolean toffee, SaltLevel saltLevel) {
		super(id, value, code, brand, measureUnit, group, productionUse, unit, clazz);
		this.numBags = numBags;
		this.grade = grade;
		this.whole = whole;
		this.roast = roast;
		this.toffee = toffee;
		this.saltLevel = saltLevel;
	}
	
	public CashewItemDTO(CashewItem cashewItem) {
		super(cashewItem);
		this.numBags = cashewItem.getNumBags();
		this.grade = cashewItem.getGrade();
		this.whole = cashewItem.isWhole();
		this.roast = cashewItem.isRoast();
		this.toffee = cashewItem.isToffee();
		this.saltLevel = cashewItem.getSaltLevel();
	}

}
