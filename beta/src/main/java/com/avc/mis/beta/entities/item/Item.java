/**
 * 
 */
package com.avc.mis.beta.entities.item;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.ValueInterface;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.validation.groups.PositiveAmount;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * Item entity, has a name-value, item's default measure unit, 
 * supply group to figure what suppliers can supply it 
 * and item category in order to infer what items can be used in various processes.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "ITEMS")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("abstract")
public class Item extends ValueEntity implements ValueInterface {
	
	@Column(nullable = false, insertable = false, updatable = false)
	private String dtype;
		
	@Setter(value = AccessLevel.PROTECTED)
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	@NotNull(message = "Item has to have a default measure unit")
	private MeasureUnit defaultMeasureUnit;
	
	@Setter(value = AccessLevel.PROTECTED)
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="unitAmount", nullable = false, 
                           	precision = 19, scale = MeasureUnit.SCALE)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(nullable = false))
    })
	@Embedded
	@NotNull(message = "Unit amount is mandatory")
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit unit;


	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Item group is mandatory")
	private ItemGroup itemGroup;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Item production use/stage is mandatory")
	private ProductionUse productionUse;

}
