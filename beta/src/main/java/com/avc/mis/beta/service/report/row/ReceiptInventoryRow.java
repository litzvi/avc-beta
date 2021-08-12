/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ReceiptInventoryRow {

	String supplier;
//	CashewItemDTO cashewItem;
	String productCompany;
	String item;
	String poCode;
	LocalDateTime receiptDate;
	ProductionFunctionality productionFunctionality;
	String[] bags;
	AmountWithUnit amount;
	String[] warehouses;
	AmountWithCurrency unitPrice;
	Currency currency;
	ProcessStatus status;

		
	public ReceiptInventoryRow(String supplier, String productCompany, 
			String item,
//			Integer itemId, String itemValue, 
//			String itemCode, String itemBrand, MeasureUnit itemMeasureUnit, ItemGroup itemGroup, ProductionUse itemProductionUse, 
//			AmountWithUnit itemUnit, Class<? extends Item> itemClazz, 
//			int itemNumBags, CashewGrade itemGrade, boolean itemWhole, boolean itemRoast, boolean itemToffee, SaltLevel itemSaltLevel, 
			String poCode, LocalDateTime receiptDate, ProductionFunctionality productionFunctionality, 
			String bags,
			BigDecimal amount, MeasureUnit measureUnit,
			String warehouses, 
			AmountWithCurrency unitPrice, Currency currency, ProcessStatus status) {
		super();
		this.supplier = supplier;
		this.productCompany = productCompany;
		this.item = item;
//		this.cashewItem = new CashewItemDTO(itemId, itemValue, itemCode, itemBrand, itemMeasureUnit, itemGroup, itemProductionUse, itemUnit, itemClazz, 
//				itemNumBags, itemGrade, itemWhole, itemRoast, itemToffee, itemSaltLevel);
		this.poCode = poCode;
		this.receiptDate = receiptDate;
		this.productionFunctionality = productionFunctionality;
		if(bags != null) {
//			this.bags = Stream.of(bags.split(",")).toArray(String[]::new);
			this.bags = Stream.of(bags.split(","))
					.map(s -> {
						String[] array = s.split("x");						
						return new Object[] {Double.valueOf(array[0]), array[1]};
					})
					.collect(Collectors.groupingBy(array -> (String)array[1], LinkedHashMap::new, Collectors.summingDouble(array -> (Double)array[0])))
					.entrySet().stream().map(entry -> Math.round(entry.getValue()) + "x" + entry.getKey()).toArray(String[]::new);
		}
		else {
			this.bags = null;
		}
		this.amount = new AmountWithUnit(amount, measureUnit).setScale(MeasureUnit.SCALE);
//		this.amount.setScale(MeasureUnit.SCALE);
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
		this.unitPrice = unitPrice;
		this.currency = currency;
		this.status = status;
	} 
	
	public BigDecimal getWeightInLbs() {
		if(getAmount().getMeasureUnit() == MeasureUnit.LBS) {
			return getAmount().getAmount();
		}
		
		try {
			return getAmount().convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE).getAmount();
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

}
