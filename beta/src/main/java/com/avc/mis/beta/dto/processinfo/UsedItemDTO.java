package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsedItemDTO extends ProcessDTO {

	@EqualsAndHashCode.Exclude
	private BasicValueEntity<Item> item;
	@EqualsAndHashCode.Exclude
	private PoCodeDTO itemPo;
	private BigDecimal numberUnits;

	//for equals comparing - since storage is excluded
	private Integer storageId;
	@EqualsAndHashCode.Exclude
	private StorageDTO storage;

	@EqualsAndHashCode.Exclude
	private Integer ordinal;	
	@EqualsAndHashCode.Exclude
	private AmountWithUnit unitAmount;
	@EqualsAndHashCode.Exclude
	private BasicValueEntity<Warehouse> warehouseLocation;
	@EqualsAndHashCode.Exclude
	private BigDecimal containerWeight;	
	
	private Warehouse NewLocation;
	
	
	public UsedItemDTO(Integer id, Integer version, BigDecimal numberUnits,
			Integer itemId, String itemValue, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal storageNumberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
		super(id, version);
		this.numberUnits = numberUnits;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		if(poCodeId != null)
			this.itemPo = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		else
			this.itemPo = null;
		this.storageId = storageId;
		this.storage = new StorageDTO(storageId, stoageVersion, storageOrdinal, 
				unitAmount, measureUnit, storageNumberUnits, containerWeight, warehouseLocationId, warehouseLocationValue, 
				storageRemarks, null);

		this.ordinal = storageOrdinal;
		this.unitAmount = new AmountWithUnit(unitAmount.setScale(MeasureUnit.SCALE), measureUnit);
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;
		this.containerWeight = containerWeight;
	
	}

	public UsedItemDTO(UsedItem usedItem) {
		super(usedItem.getId(), usedItem.getVersion());
		this.numberUnits = usedItem.getNumberUnits();
		Storage storage = usedItem.getStorage();
		ProcessItem processItem = storage.getProcessItem();
		if(processItem != null) {
			this.item = new BasicValueEntity<Item>(processItem.getItem());
			this.itemPo = new PoCodeDTO((processItem.getProcess()).getPoCode());
		}
		this.storageId = storage.getId();
		this.storage = new StorageDTO(storage);

		this.ordinal = storage.getOrdinal();
		this.unitAmount = Optional.ofNullable(storage.getUnitAmount()).map(i -> i.setScale(MeasureUnit.SCALE)).orElse(null);
		if(storage.getWarehouseLocation() != null) {
			this.warehouseLocation = new BasicValueEntity<Warehouse>(
					storage.getWarehouseLocation().getId(),  storage.getWarehouseLocation().getValue());
		}
		else {
			this.warehouseLocation = null;
		}
		this.containerWeight = storage.getContainerWeight();

	}

	
}
