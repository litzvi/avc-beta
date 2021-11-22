/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.collectionItems.CountAmountDTO;
import com.avc.mis.beta.dto.process.group.ItemCountDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;

/**
 * For fetching with query CountAmounts with the referenced ItemCount.
 * 
 * @author zvi
 *
 */
@Data
public class ItemCountWithAmount {

	private ItemCountDTO itemCount;
	private PoCodeBasic po;
	private CountAmountDTO amount;
	
	/**
	 * @param id the ProcessItem id
	 */
	public ItemCountWithAmount(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> clazz,
			MeasureUnit measureUnit, BigDecimal containerWeight, BigDecimal accessWeight,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			Integer amountId, Integer amountVersion, Integer amountOrdinal, BigDecimal amount) {
		this.itemCount = new ItemCountDTO(id, version, ordinal, 
				itemId, itemValue, productionUse, clazz, measureUnit, containerWeight, accessWeight);
		this.po = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		this.amount = new CountAmountDTO(amountId, amountVersion, amountOrdinal, amount);		
	}
	
}
