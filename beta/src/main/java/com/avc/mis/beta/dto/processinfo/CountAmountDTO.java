/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.CountAmount;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO class with the amount(weight). Part of a collection in ItemCountDTO class.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CountAmountDTO extends SubjectDataDTO {

	private BigDecimal amount;
	
	public CountAmountDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount) {
		super(id, version, ordinal);
		this.amount = amount;
	}

	public CountAmountDTO(CountAmount countAmount) {
		super(countAmount.getId(), countAmount.getVersion(), countAmount.getOrdinal());
		this.amount = countAmount.getAmount().setScale(MeasureUnit.SCALE);
	}

}
