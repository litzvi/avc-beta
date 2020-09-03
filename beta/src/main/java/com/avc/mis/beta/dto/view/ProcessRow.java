/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessRow extends ValueDTO {

	private PoCodeBasic poCode;
	private OffsetDateTime recordedTime;
	private Duration duration;
	 
	private List<ProductionProcessWithItemAmount> usedItems;
	private List<ProductionProcessWithItemAmount> producedItems;
	
	
	public ProcessRow(@NonNull Integer id, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			OffsetDateTime recordedTime, Duration duration) {
		super(id);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.recordedTime = recordedTime;
		this.duration = duration;
		
	}
	
	public AmountWithUnit[] getProcessGain() {
//		if(getUsedItems() == null)
//			return null;
		AmountWithUnit usedAmounts = getUsedItems().stream()
				.map(i -> i.getAmountWithUnit()[0])
				.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
		AmountWithUnit producedAmounts = getProducedItems().stream()
				.map(i -> i.getAmountWithUnit()[0])
				.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
		AmountWithUnit[] processGain = new AmountWithUnit[2];
		AmountWithUnit diff = producedAmounts.substract(usedAmounts);
		processGain[0] = diff.setScale(MeasureUnit.SCALE);
		processGain[1] = diff.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
		return processGain;
	}
}
