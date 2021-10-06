/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SaltLevel;
import com.avc.mis.beta.entities.item.CashewItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.values.CashewGrade;

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
	BasicValueEntity<CashewGrade> grade;
	boolean whole;
	boolean roast;
	boolean toffee;
	SaltLevel saltLevel;
	
	
	
	public CashewItemDTO(Integer id, String value, 
			String code, String brand, MeasureUnit measureUnit, ItemGroup group, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz, 
			int numBags, 
			Integer gradeId,  String gradeValue,
			boolean whole, boolean roast, boolean toffee, SaltLevel saltLevel) {
		super(id, value, code, brand, measureUnit, group, productionUse, unit, clazz);
		this.numBags = numBags;
		if(gradeId != null && gradeValue != null)
			this.grade = new BasicValueEntity<CashewGrade>(gradeId, gradeValue);
		else
			this.grade = null;
		this.whole = whole;
		this.roast = roast;
		this.toffee = toffee;
		this.saltLevel = saltLevel;
	}
	
	public CashewItemDTO(CashewItem cashewItem) {
		super(cashewItem);
		this.numBags = cashewItem.getNumBags();
		this.grade = new BasicValueEntity<CashewGrade>(cashewItem.getGrade());
		this.whole = cashewItem.isWhole();
		this.roast = cashewItem.isRoast();
		this.toffee = cashewItem.isToffee();
		this.saltLevel = cashewItem.getSaltLevel();
	}

}
