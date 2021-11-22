/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for bank
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WarehouseDTO extends ValueDTO {
	
	private String value;
	private BigDecimal weightCapacityKg;
	private BigDecimal volumeSpaceM3;
	
	public WarehouseDTO(Integer id, String value, BigDecimal weightCapacityKg, BigDecimal volumeSpaceM3) {
		super(id);
		this.value = value;
		this.weightCapacityKg = weightCapacityKg;
		this.volumeSpaceM3 = volumeSpaceM3;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Warehouse.class;
	}
	
	@Override
	public Warehouse fillEntity(Object entity) {
		Warehouse warehouseEntity;
		if(entity instanceof Warehouse) {
			warehouseEntity = (Warehouse) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Warehouse class");
		}
		super.fillEntity(warehouseEntity);
		warehouseEntity.setValue(getValue());
		warehouseEntity.setWeightCapacityKg(getWeightCapacityKg());
		warehouseEntity.setVolumeSpaceM3(getVolumeSpaceM3());
		return warehouseEntity;
	}

}
