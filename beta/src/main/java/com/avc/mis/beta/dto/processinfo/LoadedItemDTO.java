/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.PoCodeDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.processinfo.LoadedItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoadedItemDTO extends SubjectDataDTO {
	
	private ItemDTO item; //change to itemDTO in order to get category
	private PoCodeDTO poCode;
	private AmountWithUnit declaredAmount;
	
	private String description;
	private String remarks;
		
	public LoadedItemDTO(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> clazz,		
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			BigDecimal declaredAmount, MeasureUnit measureUnit,
			String description, String remarks) {
		super(id, version, ordinal);
		this.item = new ItemDTO(itemId, itemValue, null, null, productionUse, clazz);
		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);	
		this.declaredAmount = new AmountWithUnit(declaredAmount.setScale(MeasureUnit.SCALE), measureUnit);
		this.description = description;
		this.remarks = remarks;
	}	
	
	/**
	 * @param processItem
	 */
	public LoadedItemDTO(LoadedItem loadedItem) {
		super(loadedItem.getId(), loadedItem.getVersion(), loadedItem.getOrdinal());
		this.poCode = new PoCodeDTO(loadedItem.getPoCode());
		this.declaredAmount = Optional.ofNullable(loadedItem.getDeclaredAmount()).map(i -> i.setScale(MeasureUnit.SCALE)).orElse(null);
		this.description = loadedItem.getDescription();
		this.remarks = loadedItem.getRemarks();
	}

	public LoadedItemDTO(Integer id, Integer version, Integer ordinal,
			ItemDTO item, PoCodeDTO poCode, AmountWithUnit declaredAmount,
			String description, String remarks) {
		super(id, version, ordinal);
		this.item = item;
		this.declaredAmount = declaredAmount;
		this.description = description;
		this.remarks = remarks;
	}


}
