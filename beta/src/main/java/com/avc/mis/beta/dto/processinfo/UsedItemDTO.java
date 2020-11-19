package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UsedItemDTO extends UsedItemBaseDTO {

	
	//storage information - for easier access
	@EqualsAndHashCode.Exclude
	private Integer storageOrdinal;	
	@EqualsAndHashCode.Exclude
	private BigDecimal unitAmount;
	@EqualsAndHashCode.Exclude
	private BasicValueEntity<Warehouse> warehouseLocation;
	@EqualsAndHashCode.Exclude
	private BigDecimal otherUsedUnits;
	@EqualsAndHashCode.Exclude
	private BigDecimal containerWeight;	
	
	private Warehouse NewLocation;
	
	
	public UsedItemDTO(Integer id, Integer version, Integer ordinal, BigDecimal numberUsedUnits,
			Integer itemId, String itemValue, MeasureUnit measureUnit, OffsetDateTime itemProcessDate,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal storageNumberUnits, 
			BigDecimal otherUsedUnits, BigDecimal containerWeight, 
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
		super(id, version, ordinal, numberUsedUnits,
				itemId, itemValue, measureUnit, itemProcessDate,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				storageId, stoageVersion, storageOrdinal,
				unitAmount, storageNumberUnits, containerWeight,
				warehouseLocationId,  warehouseLocationValue, storageRemarks);

		this.storageOrdinal = storageOrdinal;
		this.unitAmount = unitAmount.setScale(MeasureUnit.SCALE);
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;
		this.otherUsedUnits = otherUsedUnits;
		this.containerWeight = containerWeight;
	
	}

	public UsedItemDTO(UsedItem usedItem) {
		super(usedItem);

		StorageBaseDTO storage = getStorage();
		
		this.storageOrdinal = storage.getOrdinal();
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
