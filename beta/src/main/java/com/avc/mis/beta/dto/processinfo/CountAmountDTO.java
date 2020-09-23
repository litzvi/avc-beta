/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.Ordinal;
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
public class CountAmountDTO extends ProcessDTO implements Ordinal {

	private Integer ordinal;
	private BigDecimal amount;
	
	public CountAmountDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount) {
		super(id, version);
		this.ordinal = ordinal;
		this.amount = amount;
	}

	/**
	 * @param i
	 */
	public CountAmountDTO(CountAmount countAmount) {
		super(countAmount.getId(), countAmount.getVersion());
		this.ordinal = countAmount.getOrdinal();
		this.amount = countAmount.getAmount().setScale(MeasureUnit.SCALE);
	}

}
