/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.processinfo.StorageWithSampleDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

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
	
	public AmountWithUnit getProcessGain() {
		AmountWithUnit usedAmounts = getUsedItems().stream()
				.map(ProductionProcessWithItemAmount::getAmountWithUnit)
				.reduce(AmountWithUnit.ZERO, AmountWithUnit::add);
		AmountWithUnit producedAmounts = getProducedItems().stream()
				.map(ProductionProcessWithItemAmount::getAmountWithUnit)
				.reduce(AmountWithUnit.ZERO, AmountWithUnit::add);
		return producedAmounts.substract(usedAmounts);
	}
}
