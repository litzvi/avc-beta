/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.inventory.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BasicStorageDTO extends ProcessDTO {

	private Integer ordinal;
	private BigDecimal amount;
	
	public BasicStorageDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount) {
		super(id, version);
		this.ordinal = ordinal;
		this.amount = amount;
	}
	
	public BasicStorageDTO(Integer id, Integer version) {
		super(id, version);
	}

	public BasicStorageDTO(@NonNull Storage storage) {
		super(storage.getId(), storage.getVersion());
		this.ordinal = storage.getOrdinal();
		this.amount = storage.getNumberUnits().setScale(MeasureUnit.SCALE);;
	}
}
