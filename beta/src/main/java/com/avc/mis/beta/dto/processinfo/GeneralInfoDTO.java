/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.time.Instant;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.GeneralInfoEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class GeneralInfoDTO extends DataDTO {

	private PoCodeBasic poCode;
	private String supplierName;
	private String title;
	private Integer processId;
	private ProcessName processName;
	private String processType;
	private Instant createdDate;
	private String modifiedBy;

	
	public GeneralInfoDTO(Integer id, Integer version, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			String title, Integer processId, ProcessName processName, String processType, 
			Instant createdDate, String modifiedBy) {
		super(id, version);
		if(poCodeId != null)
			this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.supplierName = supplierName;
		this.title = title;
		this.processId = processId;
		this.processName = processName;
		this.processType = processType;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
	}
	
	public GeneralInfoDTO(@NonNull GeneralInfoEntity infoEntity) {
		super(infoEntity.getId(), infoEntity.getVersion());
		if(infoEntity.getProcess() instanceof PoProcess) {
			PoCode poCode = ((PoProcess)infoEntity.getProcess()).getPoCode();
			this.poCode = new PoCodeBasic(poCode);
			this.supplierName = poCode.getSupplier().getName(); 
		}
		this.title = infoEntity.getDescription();
		this.processId = infoEntity.getProcess().getId();
		this.processName = infoEntity.getProcess().getProcessType().getProcessName();
		this.processType = infoEntity.getProcess().getProcessType().getValue();
		this.createdDate = infoEntity.getCreatedDate();
		this.modifiedBy = infoEntity.getModifiedBy().getPerson().getName();
	}
}
