/**
 * 
 */
package com.avc.mis.beta.dto.query;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.processinfo.WeightedPo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Data
public class UsedProcessWithPoCode {

	private PoCodeBasic poCode;
	private ProcessBasic<? extends PoProcess> usedProcess;

	public UsedProcessWithPoCode(
			Integer poCodeId, String poCodeCode, 
			String contractTypeCode, String contractTypeSuffix, String supplierName, 
			Integer usedProcessId, Integer usedProcessVersion, ProcessName processName, Class<? extends PoProcess> ProcessClazz
			) {
		super();
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		this.usedProcess = new ProcessBasic<>(usedProcessId, usedProcessVersion, processName, ProcessClazz);
	}
	
	@JsonIgnore
	public WeightedPo getWeightedPo() {
		WeightedPo weightedPo = new WeightedPo();
		GeneralPoCode poCode = new GeneralPoCode();
		poCode.setId(getPoCode().getId());
		weightedPo.setPoCode(poCode);
		weightedPo.setUsedProcess(getUsedProcess().getProcess());
		return weightedPo;
	}
}
