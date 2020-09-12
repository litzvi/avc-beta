/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.time.Instant;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying ApprovalTask entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ApprovalTaskDTO extends DataDTO {

	private PoCodeBasic poCode;
	private String title;
	private Integer processId;
	private ProcessName processName;
	private Instant createdDate;
	private String userName;
	private String modifiedBy;
	private String decisionType;
	private String processSnapshot;
	
	public ApprovalTaskDTO(Integer id, Integer version, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix,
			String title, Integer processId, ProcessName processName, 
			Instant createdDate, String userName, String modifiedBy, DecisionType decision, String processSnapshot) {
		super(id, version);
		if(poCodeId != null)
			this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.title = title;
		this.processId = processId;
		this.processName = processName;
		this.createdDate = createdDate;
		this.userName = userName;
		this.modifiedBy = modifiedBy;
		this.decisionType = decision.name();
		this.processSnapshot = processSnapshot;
	}
	
	public ApprovalTaskDTO(@NonNull ApprovalTask approval) {
		super(approval.getId(), approval.getVersion());
		if(approval.getProcess() instanceof PoProcess)
			this.poCode = new PoCodeBasic(((PoProcess)approval.getProcess()).getPoCode());
		this.title = approval.getDescription();
		this.processId = approval.getProcess().getId();
		this.processName = approval.getProcess().getProcessType().getProcessName();
		this.createdDate = approval.getCreatedDate();
		this.userName = approval.getUser().getPerson().getName();
		this.modifiedBy = approval.getModifiedBy().getPerson().getName();
		this.decisionType = approval.getDecision().name();
		this.processSnapshot = approval.getProcessSnapshot();
	}
}
