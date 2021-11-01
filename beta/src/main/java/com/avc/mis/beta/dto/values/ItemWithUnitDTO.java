/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ItemWithUnitDTO extends ValueDTO {

	private String value;
	private MeasureUnit measureUnit;
	private ItemGroup group;
	private ProductionUse productionUse;
	private AmountWithUnit unit;
	private Class<? extends Item> clazz;
	
	public ItemWithUnitDTO(Integer id) {
		super(id);
	}

	public ItemWithUnitDTO(Integer id, String value, MeasureUnit measureUnit, 
			ItemGroup group, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz) {
		super(id);
		this.value = value;
		this.measureUnit = measureUnit;
		this.group = group;
		this.productionUse = productionUse;
		this.unit = unit;
		this.clazz = clazz;
	}
	
	public ItemWithUnitDTO(@NonNull Item item) {
		super(item.getId());
		this.value = item.getValue();
		this.measureUnit = item.getMeasureUnit();
		this.group = item.getItemGroup();
		this.productionUse = item.getProductionUse();
		this.unit = item.getUnit();
		this.clazz = item.getClass();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Item.class;
	}
	

	@Override
	public Item fillEntity(Object entity) {
		Item item;
		if(entity instanceof Item) {
			item = (Item) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Item class");
		}
		super.fillEntity(item);
		item.setValue(getValue());
		item.setMeasureUnit(getMeasureUnit());
		item.setItemGroup(getGroup());
		item.setProductionUse(getProductionUse());
		item.setUnit(getUnit());

		
		return item;
	}

}
