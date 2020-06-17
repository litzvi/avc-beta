/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.ValueInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private String value;
	
	private BigDecimal weightCapacityKg;
	
	private BigDecimal volumeSpaceM3;
	
	public void setValue(String value) {
		this.value = value.trim();
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Warehouse name can't be blank";
	}

}
