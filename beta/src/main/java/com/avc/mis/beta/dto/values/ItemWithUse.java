/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ItemWithUse extends ValueDTO {

	String value;
	ProductionUse productionUse;
	Class<? extends Item> clazz;
	
	public ItemWithUse(Integer id, String value, ProductionUse productionUse, Class<? extends Item> clazz) {
		super(id);
		this.value = value;
		this.productionUse = productionUse;
		this.clazz = clazz;
	}
	
	public ItemWithUse(@NonNull Item item) {
		super(item.getId());
		this.value = item.getValue();
		this.productionUse = item.getProductionUse();
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
		
		return item;
	}


}
