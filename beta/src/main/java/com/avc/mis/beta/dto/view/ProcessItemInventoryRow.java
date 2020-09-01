/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ProcessItemInventoryRow extends ValueDTO {

	private ItemDTO item;
	private PoCodeDTO poCode;
	private OffsetDateTime receiptDate;
	private AmountWithUnit[] totalBalance;
	private String storages;

	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor.
	 */
	public ProcessItemInventoryRow(Integer id, Integer itemId, String itemValue, ItemCategory itemCategory,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			OffsetDateTime receiptDate,
			BigDecimal totalStoredAmount, BigDecimal totalUsedAmount, MeasureUnit measureUnit,
			String storages) {
		super(id);
		this.item = new ItemDTO(itemId, itemValue, null, null, itemCategory);
		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		this.receiptDate = receiptDate;
		this.totalBalance = new AmountWithUnit[2];
		this.totalBalance[0] = new AmountWithUnit(totalStoredAmount.subtract(totalUsedAmount), measureUnit);
		this.totalBalance[1] = this.totalBalance[0].convert(MeasureUnit.LOT);
		this.storages = storages;
	}
}
