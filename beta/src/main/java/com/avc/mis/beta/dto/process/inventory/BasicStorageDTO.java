/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.inventory.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
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
	
	public BasicStorageDTO(Integer id, Integer version) {
		super(id, version, null);
	}

	public BasicStorageDTO(@NonNull Storage storage) {
		super(storage.getId(), storage.getVersion(), storage.getOrdinal());
		this.amount = storage.getNumberUnits().setScale(MeasureUnit.SCALE);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Storage.class;
	}
}
