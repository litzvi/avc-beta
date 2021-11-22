/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.storages.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a storage cell in StorageTableDTO.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BasicStorageDTO extends SubjectDataDTO {

	private BigDecimal amount;
	private BigDecimal numberAvailableUnits;
	
	public BasicStorageDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount) {
		super(id, version, ordinal);
		this.amount = amount;
	}

	public BasicStorageDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount, BigDecimal numberAvailableUnits) {
		super(id, version, ordinal);
		this.amount = amount;
		this.numberAvailableUnits = numberAvailableUnits;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Storage.class;
	}
}
