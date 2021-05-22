/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class ReportLine {
	
	private List<ProcessStateInfo> processes;
	private Set<LocalDateTime> dates;

//	public void setProcesses(Stream<ProcessRow> processRows) {
//		this.processes = processRows.map(
//				r -> new ProcessStateInfo(r.getRecordedTime().toLocalDate(), r.getStatus(), r.getApprovals())).collect(Collectors.toList());
//		this.dates = this.processes.stream().map(r -> r.getDate()).collect(Collectors.toSet());
//	}
	
	public void setProcesses(List<ProcessStateInfo> processes) {
		this.processes = processes;
		this.dates = processes.stream().map(r -> r.getDate()).collect(Collectors.toSet());
	}

}
