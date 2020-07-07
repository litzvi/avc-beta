/**
 * 
 */
package com.avc.mis.beta.entities.values;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
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
@Entity
@Table(name="CONTRACT_TYPES")
public class ContractType extends ValueEntity {

	@Column(unique = true, nullable = false)
	@NotBlank(message = "Contract type name is mandatory")
	private String name;
	
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(name = "code", unique = true, nullable = false)
	@NotNull(message = "Contract type has to have a code")
	private ContractTypeCode code;
	
	public void setValue(String value) {
		if(value != null)
			this.code = ContractTypeCode.valueOf(value);
	}
	
	public String getValue() {
		return code.name();
	}

}
