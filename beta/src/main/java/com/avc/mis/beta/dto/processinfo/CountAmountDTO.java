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

	/**
	 * @param i
	 */
	public CountAmountDTO(CountAmount countAmount) {
		super(countAmount.getId(), countAmount.getVersion(), countAmount.getOrdinal());
		this.amount = countAmount.getAmount().setScale(MeasureUnit.SCALE);
	}

}
