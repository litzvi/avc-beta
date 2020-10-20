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
import com.avc.mis.beta.entities.ValueInterface;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SupplyGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Item entity, has a name-value, item's default measure unit, 
 * supply group to figure what suppliers can supply it 
 * and item category in order to infer what items can be used in various processes.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "ITEMS")
public class Item extends ValueEntity implements ValueInterface {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Item has to have a default measure unit")
	private MeasureUnit measureUnit;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Item supply group is mandatory")
	private SupplyGroup supplyGroup;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Item category is mandatory")
	private ItemCategory category;

}
