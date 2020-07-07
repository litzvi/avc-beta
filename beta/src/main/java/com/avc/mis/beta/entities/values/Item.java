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
import com.avc.mis.beta.entities.ValueInterface;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SupplyGroup;

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
@Table(name = "ITEMS")
public class Item extends ValueEntity implements ValueInterface {

	@Column(name = "name", unique = true, nullable = false)
	@NotBlank(message = "Item name(value) can't be blank")
	private String value;
	
	@Enumerated(EnumType.STRING)
	@Column(updatable = false, nullable = false)
	@NotNull(message = "Item has to have a default measure unit")
	private MeasureUnit measureUnit;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SupplyGroup supplyGroup;

}
