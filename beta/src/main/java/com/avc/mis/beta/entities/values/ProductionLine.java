/**
 * 
 */
package com.avc.mis.beta.entities.values;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.ValueEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Production line entity - includes a unique name for every production line.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="PRODUCTION_LINES")
public class ProductionLine extends ValueEntity {

	@Column(name = "name", unique = true, nullable = false)
	@NotBlank(message = "Production line name(value) can't be blank")
	private String value;

}
