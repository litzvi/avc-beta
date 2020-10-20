/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.ValueInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Warehouse entity - used for saving the warehouses in the plant.
 * Could also include virtual warehouses. e.g. loaded/ocean etc.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="WAREHOUSE_LOCATIONS")
public class Warehouse extends ValueEntity implements ValueInterface {
		
	private BigDecimal weightCapacityKg;
	
	private BigDecimal volumeSpaceM3;
	
	public Warehouse(Integer id, String value) {
		super();
		setId(id);
		setValue(value);
	}
	
	
	
	public Warehouse(Integer id) {
		super();
		setId(id);
	}
	
}
