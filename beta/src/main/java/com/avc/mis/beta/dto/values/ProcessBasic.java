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
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProcessBasic extends ValueDTO {
	
	ProcessName processName;

	/**
	 * @param id
	 * @param processName
	 */
	public ProcessBasic(@NonNull Integer id, ProcessName processName) {
		super(id);
		this.processName = processName;
	}
	
}
