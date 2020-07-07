/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.ValueInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
//@BatchSize(size = BaseEntity.BATCH_SIZE)
@Entity
@Table(name="WAREHOUSE_LOCATIONS")
public class Warehouse extends ValueEntity implements ValueInterface {
	
	@Column(name = "name", nullable = false)
	@NotBlank(message = "Warehouse name(value) can't be blank")
	private String value;
	
	private BigDecimal weightCapacityKg;
	
	private BigDecimal volumeSpaceM3;
	
	public void setValue(String value) {
		this.value = value.trim();
	}

}
