/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.basic.ItemWithUnit;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.storages.Storage;
import com.avc.mis.beta.entities.process.storages.UsedItemBase;
import com.avc.mis.beta.entities.values.CashewGrade;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents use of an item in inventory.
 * As using the item in production or moving in relocation.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public abstract class UsedItemBaseDTO extends RankedAuditedDTO {

	//UsedItemBase fields
	private BigDecimal numberUsedUnits;
	@EqualsAndHashCode.Exclude
	private StorageBaseDTO storage;
//	private DataObject<StorageBase> storage;

	//storage processItem info - for display on edit
	@EqualsAndHashCode.Exclude
	private ItemWithUnit item;
	@EqualsAndHashCode.Exclude
	private MeasureUnit measureUnit;
	@EqualsAndHashCode.Exclude
	private LocalDateTime itemProcessDate;
	@EqualsAndHashCode.Exclude
	private PoCodeBasic itemPo;
	@EqualsAndHashCode.Exclude
	private String[] itemPoCodes;
	@EqualsAndHashCode.Exclude
	private String[] itemSuppliers;
	@EqualsAndHashCode.Exclude
	private BasicValueEntity<CashewGrade> cashewGrade;
	
	//for equals comparing - since storage is excluded
	private Integer storageId;

	@EqualsAndHashCode.Exclude
	private BigDecimal storgeOtherUsedUnits;
	
	private BasicValueEntity<Warehouse> NewLocation;
	
	public UsedItemBaseDTO(Integer id, Integer version, Integer ordinal, BigDecimal numberUsedUnits,
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			MeasureUnit measureUnit, LocalDateTime itemProcessDate,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemPoCodes, String itemSuppliers, 
			Integer gradeId,  String gradeValue,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal storageUnitAmount, BigDecimal storageNumberUnits, BigDecimal storgeOtherUsedUnits, //BigDecimal storageContainerWeight,
			Integer storageWarehouseLocationId,  String storageWarehouseLocationValue, String storageRemarks) {
		super(id, version, ordinal);
		this.numberUsedUnits = numberUsedUnits;
		this.storage = new StorageBaseDTO(storageId, stoageVersion, storageOrdinal, 
				storageUnitAmount, storageNumberUnits, //storageContainerWeight, 
				storageWarehouseLocationId, storageWarehouseLocationValue, 
				storageRemarks, null);

		this.storageId = storageId;
		this.item = new ItemWithUnit(itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz);
		this.measureUnit = measureUnit;
		this.itemProcessDate = itemProcessDate;
		if(poCodeId != null)
			this.itemPo = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		else
			this.itemPo = null;
		if(itemPoCodes != null)
			this.itemPoCodes = Stream.of(itemPoCodes.split(",")).distinct().toArray(String[]::new);
		if(itemSuppliers != null)
			this.itemSuppliers = Stream.of(itemSuppliers.split(",")).distinct().toArray(String[]::new);
		if(gradeId != null && gradeValue != null)
			this.cashewGrade = new BasicValueEntity<CashewGrade>(gradeId, gradeValue);
		else
			this.cashewGrade = null;

		this.storgeOtherUsedUnits = storgeOtherUsedUnits;
	}
	
	public BigDecimal getNumberAvailableUnits() {
		return this.storage.getNumberUnits().subtract(this.storgeOtherUsedUnits);
	}	
	
	/**
	 * Gets a new Storage with all user set fields in the DTO (excluding id, version) 
	 * with given new warehouse location.
	 * numberUnits of the new storage is the used number of units of the used item
	 * the location set is the newLocation set in this Class
	 * @return Storage with all fields besides for the ones managed by the persistence context. 
	 */
	@JsonIgnore
	public StorageDTO getNewStorage() {
		StorageDTO storage = new StorageDTO();
		StorageBaseDTO storageBase = this.getStorage();
		storage.setOrdinal(storageBase.getOrdinal());
		storage.setUnitAmount(storageBase.getUnitAmount());
//		storage.setAccessWeight(storageDTO.getAccessWeight());
		storage.setNumberUnits(this.getNumberUsedUnits());
		storage.setWarehouseLocation(this.getNewLocation());

		return storage;
	}
	
	@Override
	public UsedItemBase fillEntity(Object entity) {
		UsedItemBase usedItemBase;
		if(entity instanceof UsedItemBase) {
			usedItemBase = (UsedItemBase) entity;
		}
		else {
			throw new IllegalStateException("Param has to be UsedItemBase class");
		}
		super.fillEntity(usedItemBase);
		if(getStorage() != null) {
			usedItemBase.setStorage(getStorage().fillEntity(new Storage()));
			setStorageId(getStorage().getId()); //for test comparing
		}
		usedItemBase.setNumberUsedUnits(getNumberUsedUnits());
		
		
		return usedItemBase;
	}

	
}
