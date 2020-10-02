/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.processinfo.CountAmountDTO;
import com.avc.mis.beta.dto.processinfo.ItemCountDTO;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ItemCountWithAmount extends ValueDTO {

	private ItemCountDTO itemCount;
	private PoCodeDTO po;
	private CountAmountDTO amount;
	
	/**
	 * @param id the ProcessItem id
	 */
	public ItemCountWithAmount(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ItemCategory itemCategory,
			MeasureUnit measureUnit, BigDecimal containerWeight,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer amountId, Integer amountVersion, Integer amountOrdinal, BigDecimal amount) {
		super(id);
		this.itemCount = new ItemCountDTO(id, version, ordinal, itemId, itemValue, itemCategory, measureUnit, containerWeight);
		this.po = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		this.amount = new CountAmountDTO(amountId, amountVersion, amountOrdinal, amount);		
	}
}
