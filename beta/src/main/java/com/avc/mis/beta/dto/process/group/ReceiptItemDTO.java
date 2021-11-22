/**
 * 
 */
package com.avc.mis.beta.dto.process.group;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.data.DataObject;
import com.avc.mis.beta.dto.process.storages.ExtraAddedDTO;
import com.avc.mis.beta.dto.process.storages.StorageDTO;
import com.avc.mis.beta.dto.process.storages.StorageWithSampleDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.process.collectionItems.OrderItem;
import com.avc.mis.beta.entities.process.group.ReceiptItem;
import com.avc.mis.beta.entities.process.storages.ExtraAdded;
import com.avc.mis.beta.entities.process.storages.Storage;
import com.avc.mis.beta.entities.process.storages.StorageWithSample;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO that contains a group of storages for a given item received in the owning process.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class ReceiptItemDTO extends ProcessItemDTO {
	
	private AmountWithUnit receivedOrderUnits;
	private AmountWithCurrency unitPrice;
	private DataObject<OrderItem> orderItem;
	private AmountWithUnit extraRequested;
	
	private List<ExtraAddedDTO> extraAdded;

	@JsonIgnore
	@EqualsAndHashCode.Exclude
	private Integer referencedOrder;

	
	public ReceiptItemDTO(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, 
			AmountWithUnit unit, Class<? extends Item> clazz,
			MeasureUnit measureUnit,
			String groupName, String description, String remarks, boolean tableView,
			BigDecimal orderUnits, MeasureUnit orderMU, BigDecimal unitPrice, Currency currency,
			Integer referencedOrder,
			Integer orderItemId, Integer orderItemVersion, BigDecimal extraRequested, MeasureUnit extraMU) {
		super(id, version, ordinal, itemId, itemValue, productionUse, unit, clazz, 
				measureUnit, groupName, description, remarks, tableView);
		if(orderUnits != null) {
			this.receivedOrderUnits = new AmountWithUnit(orderUnits.setScale(MeasureUnit.SCALE), orderMU);
		}
		if(unitPrice != null) {
			this.unitPrice = new AmountWithCurrency(unitPrice, currency);
		}
		this.referencedOrder = referencedOrder;
		if(orderItemId != null)
			this.orderItem = new DataObject<OrderItem>(orderItemId, orderItemVersion);
		if(extraRequested != null) {
			this.extraRequested = new AmountWithUnit(extraRequested.setScale(MeasureUnit.SCALE), extraMU);
		}
	}
	
	public void setStorageForms(StorageWithSampleDTO[] storageForms) {
		List<StorageDTO> receiptStorages = new ArrayList<>();
		List<ExtraAddedDTO> extraAdded = new ArrayList<>();
		Arrays.stream(storageForms).forEach(e -> {
			if(e instanceof ExtraAddedDTO) {
				extraAdded.add((ExtraAddedDTO) e);
			}
			else {
				receiptStorages.add(e);
			}
		});
		receiptStorages.addAll(extraAdded);
		super.setStorageForms(receiptStorages.isEmpty() ? null : receiptStorages);
//		setExtraAdded(extraAdded.isEmpty() ? null : extraAdded);
	}
	
	public void setStorageWithSamples(List<StorageWithSampleDTO> storageForms) {
		setStorageForms(storageForms.toArray(new StorageWithSampleDTO[storageForms.size()]));
	}
	
	public AmountWithUnit getReceivedOrderUnits() {
		if(this.receivedOrderUnits == null) {
			return null;
		}
		return this.receivedOrderUnits.setScale(MeasureUnit.SCALE);		
	}
	
	public AmountWithUnit getExtraRequested() {
		if(this.extraRequested == null) {
			return null;
		}
		return this.extraRequested.setScale(MeasureUnit.SCALE);		
	}

	public Optional<BigDecimal> getTotalDifferance() {
		return getStorageFormsField().stream()
				.map(s -> ((StorageWithSampleDTO)s).getWeighedDifferance())
				.filter(d -> d != null)
				.reduce(BigDecimal::add);
	}
	
	@Override
	public ReceiptItem fillEntity(Object entity) {
		ReceiptItem receiptItem;
		if(entity instanceof ReceiptItem) {
			receiptItem = (ReceiptItem) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ReceiptItem class");
		}
		super.fillEntity(receiptItem);
		if(getOrderItem() != null)
			receiptItem.setOrderItem((OrderItem) getOrderItem().fillEntity(new OrderItem()));
		receiptItem.setReceivedOrderUnits(getReceivedOrderUnits());
		receiptItem.setUnitPrice(getUnitPrice());
		receiptItem.setExtraRequested(getExtraRequested());
		List<Storage> storageWithExtra = new ArrayList<>();
		if(getStorageFormsField() == null || getStorageFormsField().isEmpty()) {
			throw new IllegalArgumentException("Receipt line has to contain at least one storage line");
		}
		else {
			Ordinal.setOrdinals(getStorageFormsField());
			storageWithExtra.addAll(getStorageFormsField().stream().map(i -> i.fillEntity(new StorageWithSample())).collect(Collectors.toList()));
		}
		if(getExtraAdded() != null) {
			Ordinal.setOrdinals(getExtraAdded());
			storageWithExtra.addAll(getExtraAdded().stream().map(i -> i.fillEntity(new ExtraAdded())).collect(Collectors.toList()));
		}
		receiptItem.setStorageForms(storageWithExtra.stream().collect(Collectors.toSet()));

		return receiptItem;
	}

}
