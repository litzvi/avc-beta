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
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
	//might be wrong to initialise before fetching
	private String processSnapshot;
	private String remarks;
	
	public ApprovalTaskDTO(Integer id, Integer version, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix,
			String title, Integer processId, ProcessName processName, 
			Instant createdDate, String userName, String modifiedBy, DecisionType decision, String processSnapshot) {
		super(id, version);
		this.title = title;
		this.processId = processId;
		if(poCodeId != null)
			this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.processName = processName;
//		this.createdDate = LocalDateTime.ofInstant(createdDate, ZoneOffset.UTC);
		this.userName = userName;
		this.modifiedBy = modifiedBy;
		this.createdDate = createdDate;
		this.decisionType = decision.name();
		this.processSnapshot = processSnapshot;
	}
	
	public ApprovalTaskDTO(@NonNull ApprovalTask approval) {
		super(approval.getId(), approval.getVersion());
		this.title = approval.getDescription();
		this.processId = approval.getProcess().getId();
		if(approval.getProcess() instanceof PoProcess)
			this.poCode = new PoCodeBasic(((PoProcess)approval.getProcess()).getPoCode());
		this.processName = approval.getProcess().getProcessType().getProcessName();
//		this.createdDate = LocalDateTime.ofInstant(approval.getCreatedDate(), ZoneOffset.UTC);
		this.userName = approval.getUser().getPerson().getName();
		this.modifiedBy = approval.getModifiedBy().getPerson().getName();
		this.createdDate = approval.getCreatedDate();
		this.decisionType = approval.getDecision().name();
		this.processSnapshot = approval.getProcessSnapshot();
	}
}
