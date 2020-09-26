/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

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
	private OffsetDateTime processDate;
	private OffsetDateTime receiptDate;
	private AmountWithUnit[] totalBalance;
	private String[] warehouses;

	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor.
	 */
	public ProcessItemInventoryRow(Integer id, Integer itemId, String itemValue, ItemCategory itemCategory,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			OffsetDateTime processDate, OffsetDateTime receiptDate,
			BigDecimal totalStoredAmount, BigDecimal totalUsedAmount, MeasureUnit measureUnit,
			String warehouses) {
		super(id);
		this.item = new ItemDTO(itemId, itemValue, null, null, itemCategory);
		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		this.processDate = processDate;
		this.receiptDate = receiptDate;
		this.totalBalance = new AmountWithUnit[2];
		AmountWithUnit totalBalance = new AmountWithUnit(totalStoredAmount.subtract(totalUsedAmount), measureUnit);
		this.totalBalance[0] = totalBalance.setScale(MeasureUnit.SCALE);
		this.totalBalance[1] = totalBalance.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
	}
}
