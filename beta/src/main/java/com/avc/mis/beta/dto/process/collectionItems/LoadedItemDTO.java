/**
 * 
 */
package com.avc.mis.beta.dto.process.collectionItems;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.basic.ItemWithUse;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.process.collectionItems.LoadedItem;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoadedItemDTO extends RankedAuditedDTO {
	
	private ItemWithUse item; //change to itemDTO in order to get category
	private PoCodeBasic poCode;
	private AmountWithUnit declaredAmount;
	
	private String description;
	private String remarks;
		
	public LoadedItemDTO(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> clazz,		
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			BigDecimal declaredAmount, MeasureUnit measureUnit,
			String description, String remarks) {
		super(id, version, ordinal);
		this.item = new ItemWithUse(itemId, itemValue, productionUse, clazz);
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);	
		this.declaredAmount = new AmountWithUnit(declaredAmount.setScale(MeasureUnit.SCALE), measureUnit);
		this.description = description;
		this.remarks = remarks;
	}	
	
	public LoadedItemDTO(Integer id, Integer version, Integer ordinal,
			ItemWithUse item, PoCodeBasic poCode, AmountWithUnit declaredAmount,
			String description, String remarks) {
		super(id, version, ordinal);
		this.item = item;
		this.declaredAmount = declaredAmount;
		this.description = description;
		this.remarks = remarks;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return LoadedItem.class;
	}
	
	@Override
	public LoadedItem fillEntity(Object entity) {
		LoadedItem loadedItem;
		if(entity instanceof LoadedItem) {
			loadedItem = (LoadedItem) entity;
		}
		else {
			throw new IllegalStateException("Param has to be LoadedItem class");
		}
		super.fillEntity(loadedItem);
		if(getItem() == null) {
			throw new IllegalArgumentException("Item is mandatory");
		}
		else {
			loadedItem.setItem(getItem().fillEntity(new Item()));
		}
		if(getPoCode() != null) {
			loadedItem.setPoCode(getPoCode().fillEntity(new ProductPoCode()));
		}
		loadedItem.setDeclaredAmount(getDeclaredAmount());
		loadedItem.setDescription(getDescription());
		loadedItem.setRemarks(getRemarks());
		
		return loadedItem;
	}



}
