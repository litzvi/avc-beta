/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.basic.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class FinishedProductInventoryRow {
	
//	static final DateTimeFormatter DATE_TIME_FORMATTER = 
//	        new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE)
//	                .appendLiteral(' ')
//	                .append(DateTimeFormatter.ISO_LOCAL_TIME)
//	                .toFormatter();

	private Integer poCodeId;
     
	private ItemWithUnitDTO item;
//	private String item;
	private String[] poCodes;
	private String[] receiptDates;
	private String[] processDates;
//	private BigInteger units;
	private AmountWithUnit totalAmount;
	private String[] warehouses;
	private ProcessStatus status;
     
	private BigDecimal rawDefectsAndDamage;

	
	public FinishedProductInventoryRow(
			Integer poCodeId, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz,
			String poCodes, 
			String receiptDates, String processDates, 
			BigDecimal amount, MeasureUnit measureUnit,
			String warehouses, ProcessStatus status) {
		super();
		this.poCodeId = poCodeId;
//		this.item = item;
		this.item = new ItemWithUnitDTO(itemId, itemValue, defaultMeasureUnit, itemGroup, productionUse, unit, clazz);
		this.poCodes = Stream.of(poCodes.split(",")).toArray(String[]::new);
		this.receiptDates = Stream.of(receiptDates.split(",")).toArray(String[]::new);
//		System.out.println("db dates: " + receiptDates);
//		this.receiptDates = Stream.of(receiptDates.split(","))
//				.map(j -> LocalDateTime.parse(j, DATE_TIME_FORMATTER)).map(k -> k.toLocalDate())
//				.distinct().toArray(String[]::new);
//		System.out.println("dates: " + this.receiptDates);
		this.processDates = Stream.of(processDates.split(",")).toArray(String[]::new);
//		this.receiptDates = Stream.of(receiptDates.split(",")).map(j -> LocalDateTime.parse(j, DATE_TIME_FORMATTER)).toArray(LocalDateTime[]::new);
//		this.processDates = Stream.of(processDates.split(",")).map(j -> LocalDateTime.parse(j, DATE_TIME_FORMATTER)).toArray(LocalDateTime[]::new);
//		this.units = units.setScale(0, RoundingMode.HALF_DOWN).toBigInteger();
		this.totalAmount = new AmountWithUnit(amount, measureUnit).setScale(MeasureUnit.SCALE);
//		this.totalAmount.setScale(MeasureUnit.SCALE);
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}	
		this.status = status;
	}


	public BigDecimal getWeightInLbs() {
		AmountWithUnit weight;
		if(MeasureUnit.NONE == getItem().getUnit().getMeasureUnit() && MeasureUnit.WEIGHT_UNITS.contains(getTotalAmount().getMeasureUnit())) {
			weight = getTotalAmount();
		}
		else if(MeasureUnit.WEIGHT_UNITS.contains(getItem().getUnit().getMeasureUnit())) {
			weight = new AmountWithUnit(
					getTotalAmount().getAmount()
					.multiply(getItem().getUnit().getAmount(), MathContext.DECIMAL64), 
					getItem().getUnit().getMeasureUnit());
		}
		else {
			return null;			
		}

		if(weight.getMeasureUnit() == MeasureUnit.LBS) {
			return weight.setScale(MeasureUnit.SCALE).getAmount();
		}		
		return weight.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE).getAmount();		
	}
	
	public AmountWithUnit getBoxWeight() {
		if(MeasureUnit.NONE == getItem().getUnit().getMeasureUnit()){ 
			return null;
		}
		else {
			return getItem().getUnit();
		}
	}
	
	public BigDecimal getBoxes() {
		if(MeasureUnit.NONE == getItem().getUnit().getMeasureUnit()){ 
			return null;
		}
		else {
			return getTotalAmount().getAmount();
		}
	}
}
