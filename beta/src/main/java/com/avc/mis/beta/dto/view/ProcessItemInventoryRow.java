/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Contains full information for displaying weight balances in Inventory.
 * For every (ProcessItem, PoCode) pair contains:
 * process date, receipt date, 
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ProcessItemInventoryRow extends BasicDTO {

	private ItemDTO item;
	private PoCodeBasic poCode;
	private String supplierName;
	private OffsetDateTime processDate;
	private OffsetDateTime receiptDate;
//	private BigDecimal weightCoefficient;
	private AmountWithUnit[] totalBalance;
	private String[] warehouses;

	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor.
	 */
	public ProcessItemInventoryRow(Integer id, 
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> clazz,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, String display,
			OffsetDateTime processDate, OffsetDateTime receiptDate,
			BigDecimal weightCoefficient, BigDecimal balance, MeasureUnit measureUnit,
			String warehouses) {
		super(id);
		this.item = new ItemDTO(itemId, itemValue, null, null, productionUse, clazz);
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, display);
		this.supplierName = supplierName;
		this.processDate = processDate;
		this.receiptDate = receiptDate;
//		this.weightCoefficient = weightCoefficient;
		AmountWithUnit balanceAmount = new AmountWithUnit(balance.multiply(weightCoefficient, MathContext.DECIMAL64), measureUnit);
		this.totalBalance = new AmountWithUnit[] {
				balanceAmount.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE),
				balanceAmount.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE)
		};
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
	}
	
	@JsonIgnore
	public PoInventoryRow getPoInventoryRow() {
		return new PoInventoryRow(getPoCode());
	}
	
	@JsonIgnore
	public ItemInventoryRow getItemInventoryRow() {
		return new ItemInventoryRow(getItem());
	}
	
	
	public static AmountWithUnit[] getTotalStock(List<ProcessItemInventoryRow> poInventoryRows) {
		if(poInventoryRows == null) {
			return null;
		}
		AmountWithUnit totalStock = poInventoryRows.stream()
				.map(pi -> pi.getTotalBalance()[0])
				.reduce(AmountWithUnit::add).get();
		return new AmountWithUnit[] {
				totalStock.setScale(MeasureUnit.SCALE),
				totalStock.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
	}
}
