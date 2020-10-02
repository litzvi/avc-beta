/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.StorageBase;
import com.avc.mis.beta.entities.processinfo.UsedItemBase;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UsedItemBaseDTO extends SubjectDataDTO {

	@EqualsAndHashCode.Exclude
	private BasicValueEntity<Item> item;
	@EqualsAndHashCode.Exclude
	private PoCodeDTO itemPo;
	private BigDecimal numberUsedUnits;

	//for equals comparing - since storage is excluded
	private Integer storageId;
	@EqualsAndHashCode.Exclude
	private StorageBaseDTO storage;

	
	public UsedItemBaseDTO(Integer id, Integer version, Integer ordinal, BigDecimal numberUsedUnits,
			Integer itemId, String itemValue, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal storageNumberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
		super(id, version, ordinal);
		this.numberUsedUnits = numberUsedUnits;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		if(poCodeId != null)
			this.itemPo = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		else
			this.itemPo = null;
		this.storageId = storageId;
		this.storage = new StorageBaseDTO(storageId, stoageVersion, storageOrdinal, 
				unitAmount, measureUnit, storageNumberUnits, containerWeight, warehouseLocationId, warehouseLocationValue, 
				storageRemarks, null);
	
	}

	public UsedItemBaseDTO(UsedItemBase usedItem) {
		super(usedItem.getId(), usedItem.getVersion(), usedItem.getOrdinal());
		this.numberUsedUnits = usedItem.getNumberUsedUnits();
		StorageBase storage = usedItem.getStorage();
		ProcessItem processItem = storage.getProcessItem();
		if(processItem != null) {
			this.item = new BasicValueEntity<Item>(processItem.getItem());
			this.itemPo = new PoCodeDTO((processItem.getProcess()).getPoCode());
		}
		this.storageId = storage.getId();
		this.storage = new StorageBaseDTO(storage);

	}
}
