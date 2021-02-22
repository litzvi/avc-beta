/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ItemWithUnit;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.PackedItem;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * Contains full information for displaying weight balances in Inventory.
 * For every (ProcessItem, PoCode) pair contains:
 * process date, receipt date, 
 * 
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ProcessItemInventoryRow extends BasicDTO {

	private ItemWithUnit item;
	private PoCodeBasic poCode;
	private String supplierName;
	private OffsetDateTime processDate;
	private OffsetDateTime receiptDate;
	private BigDecimal weightCoefficient;
	private AmountWithUnit amount;
	private AmountWithUnit weight; //change to weight
	private String[] warehouses;

	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor.
	 */
	public ProcessItemInventoryRow(Integer id, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			OffsetDateTime processDate, OffsetDateTime receiptDate,
			BigDecimal weightCoefficient, BigDecimal amount, 
			String warehouses) {
		super(id);
		this.item = new ItemWithUnit(itemId, itemValue, defaultMeasureUnit, unitAmount, unitMeasureUnit, clazz);
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		this.supplierName = supplierName;
		this.processDate = processDate;
		this.receiptDate = receiptDate;
		this.weightCoefficient = weightCoefficient;
//		AmountWithUnit balanceAmount = new AmountWithUnit(totalAmount.multiply(weightCoefficient, MathContext.DECIMAL64), defaultMeasureUnit);
//		this.totalWeight = new AmountWithUnit[] {
//				balanceAmount.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE),
//				balanceAmount.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE)
//		};
		
		if(clazz == BulkItem.class) {
			this.amount = null;
			this.weight = new AmountWithUnit(amount.multiply(this.weightCoefficient, MathContext.DECIMAL64), defaultMeasureUnit);
		}
		else if(clazz == PackedItem.class){
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			this.amount.setScale(MeasureUnit.SCALE);
			this.weight = new AmountWithUnit(
					amount
					.multiply(unitAmount, MathContext.DECIMAL64)
					.multiply(this.weightCoefficient, MathContext.DECIMAL64), 
					unitMeasureUnit);
		}
		else 
		{
			throw new IllegalStateException("The class can only apply to weight items");
		}
				
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
	}
	
	public List<AmountWithUnit> getTotalBalance() {
		List<AmountWithUnit> totalBalance = new ArrayList<>();
		if(this.amount != null) {
			totalBalance.add(this.amount);
		}
		if(this.weight != null) {
			totalBalance.addAll(AmountWithUnit.weightDisplay(this.weight, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS)));
		}
		return totalBalance;
	}
	
	@JsonIgnore
	public PoInventoryRow getPoInventoryRow() {
		return new PoInventoryRow(getPoCode());
	}
	
	@JsonIgnore
	public ItemInventoryRow getItemInventoryRow() {
		return new ItemInventoryRow(getItem());
	}
	
	
	public static AmountWithUnit getTotalWeight(List<ProcessItemInventoryRow> poInventoryRows) {
		if(poInventoryRows == null) {
			return null;
		}
		return poInventoryRows.stream()
				.map(pi -> pi.getWeight())
				.reduce(AmountWithUnit::add).get();
	}
	
	public static AmountWithUnit getTotalAmount(List<ProcessItemInventoryRow> poInventoryRows) {
		if(poInventoryRows == null) {
			return null;
		}
		Map<ItemWithUnit, AmountWithUnit> itemAmountmap = poInventoryRows.stream().collect(
				Collectors.groupingBy(ProcessItemInventoryRow::getItem, 
						Collectors.reducing(null, 
								i -> i.getAmount().multiply(i.getWeightCoefficient()), 
								AmountWithUnit::addNullable)));
		Collection<AmountWithUnit> amountCollection = itemAmountmap.values();
		if(amountCollection.size() > 1) {
			throw new IllegalStateException("Total units amounts need to refer to a single item");
		}
		return amountCollection.stream().findAny().get();
	}
}
