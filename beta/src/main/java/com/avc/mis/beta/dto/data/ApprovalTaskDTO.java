/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.time.Instant;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.process.ApprovalTask;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.ProcessType;

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

	private String poCode;
	private String title;
	private Integer processId;
	private String processType;
	private Instant createdDate;
	private String userName;
	private String decisionType;
	//might be wrong to initialise before fetching
	private String processSnapshot;
	private String remarks;
	
	public ApprovalTaskDTO(Integer id, Integer version, PoCode poCode, String title, Integer processId, ProcessType processType, 
			Instant createdDate, String userName, DecisionType decision, String processSnapshot) {
		super(id, version);
		this.title = title;
		this.processId = processId;
		this.poCode = poCode.getValue();
		this.processType = processType.getValue();
//		this.createdDate = LocalDateTime.ofInstant(createdDate, ZoneOffset.UTC);
		this.userName = userName;
		this.createdDate = createdDate;
		this.decisionType = decision.name();
		this.processSnapshot = processSnapshot;
	}
	
	public ApprovalTaskDTO(@NonNull ApprovalTask approval) {
		super(approval.getId(), approval.getVersion());
		this.title = approval.getTitle();
		this.processId = approval.getProcess().getId();
		this.poCode = approval.getProcess().getPoCode().getValue();
		this.processType = approval.getProcess().getProcessType().getValue();
//		this.createdDate = LocalDateTime.ofInstant(approval.getCreatedDate(), ZoneOffset.UTC);
		this.userName = approval.getUser().getPerson().getName();
		this.createdDate = approval.getCreatedDate();
		this.decisionType = approval.getDecision().name();
		this.processSnapshot = approval.getProcessSnapshot();
	}
}
