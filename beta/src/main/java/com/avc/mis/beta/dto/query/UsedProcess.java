/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.time.LocalDateTime;

import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.collection.ProcessParent;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class UsedProcess {

	ProcessBasic<? extends PoProcess> usedProcess;
	LocalDateTime recordedTime;
	LocalDateTime usingProcessRecordedTime;

	public UsedProcess(
			Integer usedProcessId, Integer usedProcessVersion, ProcessName processName, Class<? extends PoProcess> ProcessClazz, 
			LocalDateTime recordedTime, LocalDateTime usingProcessRecordedTime) {
		super();
		this.usedProcess = new ProcessBasic<>(usedProcessId, usedProcessVersion, processName, ProcessClazz);
		this.recordedTime = recordedTime;
		this.usingProcessRecordedTime = usingProcessRecordedTime;
	}	

	@JsonIgnore
	public ProcessParent getProcessParent() {
		ProcessParent processParent = new ProcessParent();
		processParent.setUsedProcess(getUsedProcess().getProcess());
		return processParent;
	}

}
