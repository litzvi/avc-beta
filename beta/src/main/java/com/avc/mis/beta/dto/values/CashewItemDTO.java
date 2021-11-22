/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.enums.SaltLevel;
import com.avc.mis.beta.entities.values.CashewGrade;
import com.avc.mis.beta.entities.values.CashewItem;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for cashew item.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class CashewItemDTO extends ItemDTO {

	private int numBags = 1;
	private BasicValueEntity<CashewGrade> grade;
	private boolean whole = false;
	private boolean roast = false;
	private boolean toffee = false;
	private SaltLevel saltLevel = SaltLevel.NS;
	
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
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return CashewItem.class;
	}
	
	@Override
	public CashewItem fillEntity(Object entity) {
		CashewItem itemEntity;
		if(entity instanceof CashewItem) {
			itemEntity = (CashewItem) entity;
		}
		else {
			throw new IllegalStateException("Param has to be CashewItem class");
		}
		super.fillEntity(itemEntity);
		itemEntity.setNumBags(getNumBags());
		if(getGrade() != null)
			itemEntity.setGrade((CashewGrade) getGrade().fillEntity(new CashewGrade()));
		itemEntity.setWhole(isWhole());
		itemEntity.setRoast(isRoast());
		itemEntity.setToffee(isToffee());
		itemEntity.setSaltLevel(getSaltLevel());
		return itemEntity;
	}

}
