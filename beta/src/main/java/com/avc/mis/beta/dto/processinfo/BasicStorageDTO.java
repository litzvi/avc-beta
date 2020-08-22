/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BasicStorageDTO extends ProcessDTO {

	private Integer ordinal;
	private BigDecimal amount;
	
	public BasicStorageDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount) {
		super(id, version);
		this.ordinal = ordinal;
		this.amount = amount;
	}

	public BasicStorageDTO(Storage storage) {
		super(storage.getId(), storage.getVersion());
		this.ordinal = storage.getOrdinal();
		this.amount = storage.getUnitAmount().getAmount().setScale(MeasureUnit.SCALE);;
	}
}
