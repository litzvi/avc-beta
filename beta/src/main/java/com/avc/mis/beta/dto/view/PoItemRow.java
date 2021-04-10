/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.PackedItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * DTO used for report/table to represent essential order-item information,
 * including PO information of the order, to be represented or aggregated in the table.
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class PoItemRow extends BasicDTO {
	
	String personInCharge;
	PoCodeBasic poCode;
	String[] approvals;
	String supplierName;
	ItemWithUnitDTO item;
//	String itemName;
	Integer orderItemId;
	@JsonIgnore
	AmountWithUnit numUnits;
	OffsetDateTime contractDate;
	LocalDate deliveryDate;
	String defects;
	AmountWithCurrency unitPrice;
	@NonFinal @Setter BigDecimal receivedOrderUnits;
	@NonFinal @Setter BigDecimal receivedAmount;
	ProcessStatus status;
	@NonFinal @Setter Long receiptsCancelled;
//	List<String> orderStatus;
	
	/**
	 * All arguments Constructor ,
	 * used to project directly from database without nested fetching.
	 */
	public PoItemRow(@NonNull Integer id, String personInCharge,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String approvals,
			Integer itemId, String itemValue, MeasureUnit itemMeasureUnit, ItemGroup itemGroup, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz,
			Integer orderItemId, BigDecimal amount, MeasureUnit measureUnit, 
			OffsetDateTime contractDate, LocalDate deliveryDate, 
			String defects, BigDecimal unitPrice, Currency currency, 
//			BigDecimal receivedOrderUnits,
//			BigDecimal receivedAmount, 
			ProcessStatus status) {
//			, long receiptsCancelled) {
		super(id);
		this.personInCharge = personInCharge;
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		if(approvals == null || approvals.startsWith(":")) {
			this.approvals = null;
		}
		else {
			this.approvals = Stream.of(approvals.split(",")).distinct().toArray(String[]::new);
		}
		this.supplierName = supplierName;
		this.item = new ItemWithUnitDTO(itemId, itemValue, itemMeasureUnit, itemGroup, null, unitAmount, unitMeasureUnit, clazz);
//		this.itemName = itemName;
		this.orderItemId = orderItemId;
		this.numUnits = new AmountWithUnit(amount, measureUnit);
//		this.numberUnits = new AmountWithUnit[] {
//				numberUnits.setScale(MeasureUnit.SCALE), 
//				numberUnits.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
		this.contractDate = contractDate;
		this.deliveryDate = deliveryDate;
		this.defects = defects;
		if(unitPrice != null) {
			this.unitPrice = new AmountWithCurrency(unitPrice, currency);
		}
		else {
			this.unitPrice = null;
		}
//		this.receivedOrderUnits = receivedOrderUnits;
//		this.receivedAmount = receivedAmount;
		this.status = status;
//		this.receiptsCancelled = receiptsCancelled;
	}
	
	public List<String> getOrderStatus() {
		
		List<String> orderStatus = new ArrayList<String>();
		if(this.status == ProcessStatus.CANCELLED) {
			orderStatus.add("CANCELLED");
		}
		else if(this.receivedOrderUnits == null) {
			orderStatus.add("OPEN");
		}
		else {
			switch(this.receivedOrderUnits.compareTo(this.numUnits.getAmount())) {
			case -1:
				orderStatus.add("OPEN");
				if(this.receivedOrderUnits.signum() > 0) {
					orderStatus.add("PARTLY RECEIVED");
				}
				break;
			case 0:
			case 1:
				orderStatus.add("RECEIVED");
				break;
			}
		}
		
		if(this.receiptsCancelled > 0) {
			orderStatus.add("REJECTED");
		}
		
		return orderStatus;
	}
	
	public List<AmountWithUnit> getNumberUnits() {
		return AmountWithUnit.amountDisplay(this.numUnits, this.item, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
	}
	
	public AmountWithUnit getNumberLots() {
		if(item.getGroup()  == ItemGroup.PRODUCT) {
			AmountWithUnit numberLots;
			if(item.getClazz() == PackedItem.class) {
				numberLots = item.getUnit().multiply(this.numUnits.getAmount());
			}
			else {
				numberLots = this.numUnits;
			}
			return numberLots.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
		}
		else {
			return null;			
		}
	}
	
	public AmountWithUnit getBalance() {
		if(this.receivedOrderUnits == null) {
			return this.numUnits;
		}
		return this.numUnits.subtract(this.receivedOrderUnits);
	}
		
	/**
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900002V
	 */
	public String getValue() {
		return this.poCode.getValue();
	}
	
	
}
