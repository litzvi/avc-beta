/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.ProcessName;

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
public class ProcessBasic extends ValueDTO {
	
	ProcessName processName;

	public ProcessBasic(@NonNull Integer id, ProcessName processName) {
		super(id);
		this.processName = processName;
	}
	
}
