/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDate;

import com.avc.mis.beta.entities.enums.ProcessStatus;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class ProcessStateInfo {

	LocalDate date;
	ProcessStatus status;
	String approvals;
	
	public ProcessStateInfo(LocalDate date, ProcessStatus status, String approvals) {
		super();
		this.date = date;
		this.status = status;
		this.approvals = approvals;
	}

	
	
}
