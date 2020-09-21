/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.List;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemCountDTO extends ProcessDTO {

	private MeasureUnit measureUnit;
	private BigDecimal containerWeight;	
	
	private List<CountAmountDTO> amounts;

	public ItemCountDTO(Integer id, Integer version, 
			MeasureUnit measureUnit, BigDecimal containerWeight) {
		super(id, version);
		this.measureUnit = measureUnit;
		this.containerWeight = containerWeight;
	}
	
	
}
