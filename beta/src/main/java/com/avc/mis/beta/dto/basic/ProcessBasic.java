/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDataValueDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * Wrapper for a process id, contains the name of the process type. e.g. CASHEW_ORDER, CASHEW_RECEIPT
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ProcessBasic <T extends GeneralProcess> extends BasicDataValueDTO {
	
	ProcessName processName;
	Class<? extends T> processClazz;
	
	public ProcessBasic(@NonNull Integer id, ProcessName processName, Class<? extends T> processClazz) {
		this(id, null, processName, processClazz);
	}

	public ProcessBasic(@NonNull Integer id, Integer version, ProcessName processName, Class<? extends T> processClazz) {
		super(id, version);
		this.processName = processName;
		this.processClazz = processClazz;
	}
	
	@JsonIgnore
	public T getProcess() {
		T process;
		try {
			process = (T) processClazz.newInstance();
			process.setId(getId());
			process.setVersion(getVersion());
			return process;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage());
		} 
	}

	@Override
	public String getValue() {
		return this.processName.toString();
	}
	
}
