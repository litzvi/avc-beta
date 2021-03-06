/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.process.inventory.UsedItemBase;
import com.avc.mis.beta.entities.values.CashewGrade;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UsedItemBaseDTO extends SubjectDataDTO {

	//UsedItemBase fields
	private BigDecimal numberUsedUnits;
	@EqualsAndHashCode.Exclude
	private StorageDTO storage;
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

//	//storage information - for easier access
//	@EqualsAndHashCode.Exclude
//	private Integer storageOrdinal;	
//	@EqualsAndHashCode.Exclude
//	private BigDecimal storageUnitAmount;
//	@EqualsAndHashCode.Exclude
//	private BigDecimal storageNumberUnits;	
	@EqualsAndHashCode.Exclude
	private BigDecimal storgeOtherUsedUnits;
//	@EqualsAndHashCode.Exclude
//	private BigDecimal storageContainerWeight;	
//	@EqualsAndHashCode.Exclude
//	private BasicValueEntity<Warehouse> storageWarehouseLocation;
	
	private Warehouse NewLocation;


	
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
//		this.storage = new DataObject<StorageBase>(storageId, stoageVersion);
		this.storage = new StorageDTO(storageId, stoageVersion, storageOrdinal, 
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
	
//		this.storageOrdinal = storageOrdinal;
//		this.storageUnitAmount = storageUnitAmount.setScale(MeasureUnit.SCALE);
//		this.storageNumberUnits = storageNumberUnits.setScale(MeasureUnit.SCALE);
		this.storgeOtherUsedUnits = storgeOtherUsedUnits;
//		this.storageContainerWeight = storageContainerWeight;
//		if(storageWarehouseLocationId != null && storageWarehouseLocationValue != null)
//			this.storageWarehouseLocation = new BasicValueEntity<Warehouse>(storageWarehouseLocationId,  storageWarehouseLocationValue);
//		else
//			this.storageWarehouseLocation = null;
	}

	public UsedItemBaseDTO(UsedItemBase usedItem) {
		super(usedItem.getId(), usedItem.getVersion(), usedItem.getOrdinal());
		this.numberUsedUnits = usedItem.getNumberUsedUnits();
		StorageBase storage = usedItem.getStorage();
		this.storage = new StorageDTO(storage);
//		this.storage = new DataObject<StorageBase>(storage);
		this.storageId = storage.getId();
		ProcessItem processItem = storage.getProcessItem();
		if(processItem != null) {
			this.item = new ItemWithUnit(processItem.getItem());
			this.measureUnit = processItem.getMeasureUnit();
			this.itemProcessDate = processItem.getProcess().getRecordedTime();
			this.itemPo = new PoCodeBasic((processItem.getProcess()).getPoCode());
		}
		
		this.storgeOtherUsedUnits = null;//not used for testing


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
	public Storage getNewStorage() {
		Storage storage = new Storage();
		StorageDTO storageDTO = this.getStorage();
		storage.setOrdinal(storageDTO.getOrdinal());
		storage.setUnitAmount(storageDTO.getUnitAmount());
//		storage.setAccessWeight(storageDTO.getAccessWeight());
		storage.setNumberUnits(this.getNumberUsedUnits());
		storage.setWarehouseLocation(this.getNewLocation());

		return storage;
	}
	

	
}
