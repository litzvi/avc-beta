/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.QcCompany;
import com.avc.mis.beta.utilities.ListGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class QcReportLine extends ReportLine implements ListGroup<ItemQc> {

	@JsonIgnore
	private Integer processId;
	private QcCompany checkedBy;
//	OffsetDateTime checkDate;

	private List<ItemQc> itemQcs;
	
	public QcReportLine(@NonNull Integer processId, 
			QcCompany checkedBy, 
			OffsetDateTime date, ProcessStatus status, String approvals) {
		super();
		this.processId = processId;
		this.checkedBy = checkedBy;
		setProcesses(new ProcessStateInfo(date.toLocalDate(), status, approvals));
	}
	
	public void setItemQcs(List<ItemQc> itemQcs) {
		boolean empty = itemQcs == null || itemQcs.isEmpty();
		this.itemQcs = empty ? null : itemQcs;
	}

	@Override
	public Integer getId() {
		return getProcessId();
	}

	@Override
	public void setList(List<ItemQc> list) {
		setItemQcs(list);
	}

	
	
	
}
