/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ItemDTO  extends ValueDTO {

	private String value;
	private String code;
	private String brand;
	private MeasureUnit measureUnit;
	private AmountWithUnit unit;
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
	
	public ItemDTO(@NonNull Item item) {
		super(item.getId());
		this.value = item.getValue();
		this.code = item.getCode();
		this.brand = item.getBrand();
		this.measureUnit = item.getMeasureUnit();
		this.unit = item.getUnit();
		this.group = item.getItemGroup();
		this.productionUse = item.getProductionUse();
		this.clazz = item.getClass();
	}
}
