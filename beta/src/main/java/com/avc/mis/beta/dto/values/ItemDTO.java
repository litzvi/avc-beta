/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for item.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ItemDTO  extends ValueDTO {

	private String value;
	private String code;
	private String brand;
	private MeasureUnit measureUnit = MeasureUnit.UNIT;
	private AmountWithUnit unit = AmountWithUnit.NEUTRAL;
	private ItemGroup group;
	private ProductionUse productionUse;
	private Class<? extends Item> clazz;
	
	public ItemDTO(Integer id, String value, String code, String brand,
			MeasureUnit measureUnit, ItemGroup group, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz) {
		super(id);
		this.value = value;
		this.code = code;
		this.brand = brand;
		this.measureUnit = measureUnit;
		this.unit = unit;
		this.group = group;
		this.productionUse = productionUse;
		this.clazz = clazz;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Item.class;
	}
	
	@Override
	public Item fillEntity(Object entity) {
		Item itemEntity;
		if(entity instanceof Item) {
			itemEntity = (Item) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Item class");
		}
		super.fillEntity(itemEntity);
		itemEntity.setValue(getValue());
		itemEntity.setCode(getCode());
		itemEntity.setBrand(getBrand());
		itemEntity.setMeasureUnit(getMeasureUnit());
		itemEntity.setUnit(getUnit());
		itemEntity.setItemGroup(getGroup());
		itemEntity.setProductionUse(getProductionUse());
		return itemEntity;
	}
}
