/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.inventory.UsedItemBase;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BasicUsedStorageDTO extends SubjectDataDTO {
	
	Integer storageId;
	Integer storageVersion;
//	Integer ordinal; //the storage ordinal
	BigDecimal storageAmount; 
	
	BigDecimal amount; //this is numberUsedUnits change for Mei
	BigDecimal numberAvailableUnits;
	
//	StorageDTO storage;
	
//	public BasicUsedStorageDTO(Integer id, Integer version, StorageDTO storage) {
//		super(id, version);
//		this.storage = storage;
//	}
	
	/**
	 * Used for UsedItemTable
	 */
	public BasicUsedStorageDTO(Integer id, Integer version, 
			Integer storageId, Integer storageVersion, Integer ordinal, BigDecimal storageAmount, 
			BigDecimal numberUsedUnits, BigDecimal numberAvailableUnits) {
		super(id, version, ordinal);
		this.storageId = storageId;
		this.storageVersion = storageVersion;
//		this.ordinal = ordinal;
		this.storageAmount = storageAmount;
		this.amount = numberUsedUnits;
		this.numberAvailableUnits = numberAvailableUnits;
	}
	
	/**
	 * Used for MovedItemTable
	 */
//	public BasicUsedStorageDTO(Integer id, Integer version, 
//			Integer storageId, Integer storageVersion, Integer ordinal, BigDecimal storageAmount) {
//		super(id, version, ordinal);
//		this.storageId = storageId;
//		this.storageVersion = storageVersion;
////		this.ordinal = ordinal;
//		this.storageAmount = storageAmount;
//	}
	

	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return UsedItemBase.class;
	}
}
