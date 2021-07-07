/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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

	@Getter(value = AccessLevel.NONE)
	private List<MeasureUnit> weightDisplayMeasureUnits = Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS);
	
	private ItemWithUnitDTO item;
	private PoCodeBasic poCode;
	private String supplierName;
	private LocalDateTime processDate;
	private LocalDateTime receiptDate;
	private BigDecimal weightCoefficient;
	private AmountWithUnit amount;
	private AmountWithUnit weight;
	private String[] warehouses;

	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor.
	 */
	public ProcessItemInventoryRow(Integer id, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			LocalDateTime processDate, LocalDateTime receiptDate,
			BigDecimal weightCoefficient, BigDecimal amount, 
			String warehouses) {
		super(id);
		this.item = new ItemWithUnitDTO(itemId, itemValue, defaultMeasureUnit, itemGroup, productionUse, unit, clazz);
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		this.supplierName = supplierName;
		this.processDate = processDate;
		this.receiptDate = receiptDate;
		this.weightCoefficient = weightCoefficient;
		if(MeasureUnit.NONE == item.getUnit().getMeasureUnit() && MeasureUnit.WEIGHT_UNITS.contains(defaultMeasureUnit)) {
			this.amount = null;
			this.weight = new AmountWithUnit(amount.multiply(this.weightCoefficient, MathContext.DECIMAL64), defaultMeasureUnit);
		}
		else if(MeasureUnit.WEIGHT_UNITS.contains(item.getUnit().getMeasureUnit())) {
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit).setScale(MeasureUnit.SCALE);
//			this.amount.setScale(MeasureUnit.SCALE);
			this.weight = new AmountWithUnit(
					amount
					.multiply(unit.getAmount(), MathContext.DECIMAL64)
					.multiply(this.weightCoefficient, MathContext.DECIMAL64), 
					unit.getMeasureUnit());
		}
		else {
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			this.weight = null;
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
			totalBalance.addAll(AmountWithUnit.weightDisplay(this.weight, weightDisplayMeasureUnits));
		}
		return totalBalance;
	}
			
	public static AmountWithUnit getTotalWeight(List<ProcessItemInventoryRow> poInventoryRows) {
		if(poInventoryRows == null) {
			return null;
		}
		
		Optional<AmountWithUnit> optionalWeight = poInventoryRows.stream()
				.map(pi -> pi.getWeight())
				.filter(weight -> weight != null)
				.reduce(AmountWithUnit::add);
		
		if(optionalWeight.isPresent()) {
			return optionalWeight.get();
		}
		return null;
	}
	
	public static AmountWithUnit getTotalAmount(List<ProcessItemInventoryRow> poInventoryRows) {
		if(poInventoryRows == null) {
			return null;
		}
		Map<ItemWithUnitDTO, AmountWithUnit> itemAmountmap = poInventoryRows.stream().collect(
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
