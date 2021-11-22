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
 * Quality check report summary for final report.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class QcReportLine extends ProcessStateInfo {
	private String checkedBy;

	private List<ItemQc> itemQcs;
	
	public QcReportLine(@NonNull Integer id, 
			QcCompany checkedBy, 
			LocalDateTime date, ProcessStatus status, String approvals) {
		super(id, date, status, approvals);
		this.checkedBy = checkedBy.label;
	}
	
	public void setItemQcs(List<ItemQc> itemQcs) {
		boolean empty = itemQcs == null || itemQcs.isEmpty();
		this.itemQcs = empty ? null : itemQcs;
	}
	
}
