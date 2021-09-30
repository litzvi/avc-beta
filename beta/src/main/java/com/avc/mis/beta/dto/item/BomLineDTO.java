/**
 * 
 */
package com.avc.mis.beta.dto.item;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectLinkDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BillOfMaterials;
import com.avc.mis.beta.entities.item.BomLine;
import com.avc.mis.beta.entities.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BomLineDTO extends SubjectLinkDTO {

	private BasicValueEntity<Item> material;
	
	private AmountWithUnit defaultAmount;

	public BomLineDTO(Integer id, Integer ordinal, 
			Integer materialId, String meterialValue, BigDecimal defaultAmount, MeasureUnit defaultMeasureUnit) {
		super(id, ordinal);
		this.material = new BasicValueEntity<Item>(materialId, meterialValue);
		this.defaultAmount = new AmountWithUnit(defaultAmount, defaultMeasureUnit);
	}
	
	public BomLineDTO(BomLine bomLine) {
		super(bomLine);
		if(bomLine.getMaterial() != null)
			this.material = new BasicValueEntity<Item>(bomLine.getMaterial());
		if(bomLine.getDefaultAmount() != null)
			this.defaultAmount = bomLine.getDefaultAmount().clone();
	}

	@JsonIgnore
	public BomLine getEntity() {
		BomLine bomLine = new BomLine();
		bomLine.setId(getId());
		bomLine.setOrdinal(getOrdinal());
		
		if(getMaterial() != null) {
			Item material = new Item();
			material.setId(getMaterial().getId());
			material.setValue(getMaterial().getValue());
			bomLine.setMaterial(material);
		}
		if (getDefaultAmount() != null) {
			bomLine.setDefaultAmount(getDefaultAmount().clone());
		}
		return bomLine;

	}

	
	
	
	
}
