/**
 * 
 */
package com.avc.mis.beta.entities.values;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ValueEntity;

import io.micrometer.core.instrument.util.StringUtils;
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
@Entity
@Table(name="CONTRACT_TYPES")
public class ContractType extends ValueEntity {

	@Column(unique = true, nullable = false)
	private String name;
	
	@Column(name = "code", unique = true, nullable = false)
	private String value;

	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getName()) && StringUtils.isNotBlank(getValue());
	}

	@Override
	public String getIllegalMessage() {
		return "Contract type name and code can't be blank";
	}
}
