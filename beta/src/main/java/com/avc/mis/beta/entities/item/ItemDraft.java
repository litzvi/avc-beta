/**
 * 
 */
package com.avc.mis.beta.entities.item;

import javax.persistence.Column;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author zvi
 *
 */
@Data
public abstract class ItemDraft {
	
	public ItemDraft() {
		super();
		setUnit(AmountWithUnit.ONE_UNIT);
		setDefaultMeasureUnit(MeasureUnit.UNIT);
	}

	private String value;

	@Setter(value = AccessLevel.PROTECTED)
	@Column(nullable = false, updatable = false)
	private MeasureUnit defaultMeasureUnit;
	@Setter(value = AccessLevel.PROTECTED)
	@Column(nullable = false, updatable = false)
	private AmountWithUnit unit;
	
	private ItemType type;
		
	private ItemCategory category;
	
	private ItemGroup group;

	private ProductionUse stage;
	
	
}
