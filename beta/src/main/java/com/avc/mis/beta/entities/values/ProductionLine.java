/**
 * 
 */
package com.avc.mis.beta.entities.values;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(name = "productionFunctionality", nullable = false)
//	@Column(name = "productionFunctionality")
	@NotNull(message = "Production functionality mandatory, links production line to it's production functionality")
	private ProductionFunctionality productionFunctionality;

}
