/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.enums.ProcessStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessStateInfo extends BasicDTO {

	private LocalDate date;
	private ProcessStatus status;
	private String[] approvals;
	
	public ProcessStateInfo(Integer id, LocalDate date, ProcessStatus status, String[] approvals) {
		super(id);
		this.date = date;
		this.status = status;
		this.approvals = approvals;
	}

	public ProcessStateInfo(Integer id, OffsetDateTime date, ProcessStatus status, String approvals) {
		super(id);
		this.date = date.toLocalDate();
		this.status = status;
		if(approvals == null || approvals.startsWith(":")) {
			this.approvals = null;
		}
		else {
			this.approvals = Stream.of(approvals.split(",")).distinct().toArray(String[]::new);
		}
	}
	
	
}
