/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDateTime;
import java.util.List;

import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.QcCompany;

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
public class QcReportLine extends ProcessStateInfo 
//implements ListGroup<ItemQc> 
{

//	@JsonIgnore
//	private Integer processId;
	private String checkedBy;
//	OffsetDateTime checkDate;

	private List<ItemQc> itemQcs;
	
	public QcReportLine(@NonNull Integer id, 
			QcCompany checkedBy, 
			LocalDateTime date, ProcessStatus status, String approvals) {
		super(id, date, status, approvals);
		this.checkedBy = checkedBy.label;
//		setProcesses(new ProcessStateInfo(date.toLocalDate(), status, approvals));
	}
	
	public void setItemQcs(List<ItemQc> itemQcs) {
		boolean empty = itemQcs == null || itemQcs.isEmpty();
		this.itemQcs = empty ? null : itemQcs;
	}

//	@Override
//	public Integer getId() {
//		return getProcessId();
//	}

//	@Override
//	public void setList(List<ItemQc> list) {
//		setItemQcs(list);
//	}

	
	
	
}
