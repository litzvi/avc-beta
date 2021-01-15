/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDate;

import com.avc.mis.beta.entities.enums.ProcessStatus;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ProcessStateInfo {

	LocalDate date;
	ProcessStatus status;
	String approvals;
	
	
}
