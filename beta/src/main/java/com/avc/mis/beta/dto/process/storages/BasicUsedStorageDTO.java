/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.storages.UsedItemBase;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Represents a used item cell in UsedItemTableDTO.
 * 
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BasicUsedStorageDTO extends SubjectDataDTO {
	
	Integer storageId;
	Integer storageVersion;
	BigDecimal storageAmount; 
	
	BigDecimal amount; //this is numberUsedUnits change for Mei
	BigDecimal numberAvailableUnits;
	
	/**
	 * Used for UsedItemTable
	 */
	public BasicUsedStorageDTO(Integer id, Integer version, 
			Integer storageId, Integer storageVersion, Integer ordinal, BigDecimal storageAmount, 
			BigDecimal numberUsedUnits, BigDecimal numberAvailableUnits) {
		super(id, version, ordinal);
		this.storageId = storageId;
		this.storageVersion = storageVersion;
		this.storageAmount = storageAmount;
		this.amount = numberUsedUnits;
		this.numberAvailableUnits = numberAvailableUnits;
	}

	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return UsedItemBase.class;
	}
}
