/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.GeneralProcess;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * Wrapper for a process id, contains the name of the process type. e.g. CASHEW_ORDER, CASHEW_RECEIPT
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProcessBasic extends BasicDTO {
	
	ProcessName processName;
	Class<? extends GeneralProcess> ProcessClazz;

	public ProcessBasic(@NonNull Integer id, ProcessName processName, Class<? extends GeneralProcess> ProcessClazz) {
		super(id);
		this.processName = processName;
		this.ProcessClazz = ProcessClazz;
	}
	
}
