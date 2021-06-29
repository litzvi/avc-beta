/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.values.CashewItemDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.CashewGrade;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.enums.SaltLevel;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

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
			this.bags = Stream.of(bags.split(",")).toArray(String[]::new);
		}
		else {
			this.bags = null;
		}
		this.amount = new AmountWithUnit(amount, measureUnit);
		this.amount.setScale(MeasureUnit.SCALE);
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
