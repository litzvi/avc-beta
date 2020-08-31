package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

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

//	private Integer ordinal;
	private BasicValueEntity<Item> item;
	private PoCodeDTO itemPo;
	
//	private AmountWithUnit unitAmount;
//	private BasicValueEntity<Warehouse> warehouseLocation;
	private BigDecimal numberUnits;
//	private BigDecimal containerWeight;	
	
	private StorageDTO storage;


	public UsedItemDTO(Integer id, Integer version, BigDecimal numberUnits,
			Integer itemId, String itemValue, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
//			BigDecimal unitAmount, MeasureUnit measureUnit,
//			Integer warehouseLocationId,  String warehouseLocationValue,
//			 BigDecimal containerWeight,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal storageNumberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
		super(id, version);
		this.numberUnits = numberUnits;
//		this.ordinal = ordinal;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		if(poCodeId != null)
			this.itemPo = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		else
			this.itemPo = null;
//		this.unitAmount = new AmountWithUnit(unitAmount.setScale(MeasureUnit.SCALE), measureUnit);
//		if(warehouseLocationId != null && warehouseLocationValue != null)
//			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
//		else
//			this.warehouseLocation = null;
//		this.containerWeight = containerWeight;
		this.storage = new StorageDTO(storageId, stoageVersion, storageOrdinal, 
				unitAmount, measureUnit, storageNumberUnits, containerWeight, warehouseLocationId, warehouseLocationValue, 
				storageRemarks, null);
	}

	public UsedItemDTO(UsedItem usedItem) {
		super(usedItem.getId(), usedItem.getVersion());
		this.numberUnits = usedItem.getNumberUnits();
		Storage storage = usedItem.getStorage();
		ProcessItem processItem = storage.getProcessItem();
//		this.ordinal = storage.getOrdinal();
		this.item = new BasicValueEntity<Item>(processItem.getItem());
		this.itemPo = new PoCodeDTO((processItem.getProcess()).getPoCode());
//		this.unitAmount = storage.getUnitAmount().setScale(MeasureUnit.SCALE);
//		if(storage.getWarehouseLocation() != null) {
//			this.warehouseLocation = new BasicValueEntity<Warehouse>(
//					storage.getWarehouseLocation().getId(),  storage.getWarehouseLocation().getValue());
//		}
//		else {
//			this.warehouseLocation = null;
//		}
//		this.containerWeight = storage.getContainerWeight();
		this.storage = new StorageDTO(storage);
		
	}

	
}
