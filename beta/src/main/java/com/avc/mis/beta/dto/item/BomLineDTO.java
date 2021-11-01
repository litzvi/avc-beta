/**
 * 
 */
package com.avc.mis.beta.dto.item;

import com.avc.mis.beta.dto.SubjectLinkDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
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
			Integer materialId, String meterialValue, AmountWithUnit defaultAmount) {
		super(id, ordinal);
		this.material = new BasicValueEntity<Item>(materialId, meterialValue);
		this.defaultAmount = defaultAmount;
	}
	
	public BomLineDTO(BomLine bomLine) {
		super(bomLine);
		if(bomLine.getMaterial() != null)
			this.material = new BasicValueEntity<Item>(bomLine.getMaterial());
		if(bomLine.getDefaultAmount() != null)
			this.defaultAmount = bomLine.getDefaultAmount().clone();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return BomLine.class;
	}

	@Override
	public BomLine fillEntity(Object entity) {
		BomLine bomLine;
		if(entity instanceof BomLine) {
			bomLine = (BomLine) entity;
		}
		else {
			throw new IllegalStateException("Param has to be BomLine class");
		}
		super.fillEntity(bomLine);
		
		if(getMaterial() != null) {
			Item material = new Item();
			material.setId(getMaterial().getId());
			bomLine.setMaterial(material);
		}
		if (getDefaultAmount() != null) {
			bomLine.setDefaultAmount(getDefaultAmount().clone());
		}
		return bomLine;

	}

	
	
	
	
}
