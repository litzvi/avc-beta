/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * Used as a base class for report lines
 * 
 * @author zvi
 *
 */
@Data
public class ReportLine {
	
	private List<ProcessStateInfo> processes;
	private Set<LocalDateTime> dates;

	public void setProcesses(List<ProcessStateInfo> processes) {
		this.processes = processes;
		this.dates = processes.stream().map(r -> r.getDate()).collect(Collectors.toSet());
	}

}
