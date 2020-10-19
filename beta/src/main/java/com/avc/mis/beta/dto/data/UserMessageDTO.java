/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.time.Instant;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.processinfo.UserMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying UserMessage entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserMessageDTO extends DataDTO {

	private PoCodeBasic poCode;
	private String supplierName;
	private String title;
	private Integer processId;
	private ProcessName processName;
	private String processType; 
	private Instant createdDate;
	private String userName;
	private String modifiedBy;
	private String label;
	
	public UserMessageDTO(Integer id, Integer version, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			String title, Integer processId, ProcessName processName, String processType, 
			Instant createdDate, String userName, String modifiedBy, MessageLabel label) {
		super(id, version);
		if(poCodeId != null) {
			this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		}
		this.supplierName = supplierName;
		this.title = title;
		this.processId = processId;
		this.processName = processName;
		this.processType = processType;
		this.createdDate = createdDate;
		this.userName = userName;
		this.modifiedBy = modifiedBy;
		this.label = label.name();
	}
	
	public UserMessageDTO(@NonNull UserMessage message) {
		super(message.getId(), message.getVersion());
			if(message.getProcess() instanceof PoProcess) {
			PoCode poCode = ((PoProcess)message.getProcess()).getPoCode();
			this.poCode = new PoCodeBasic(poCode);
			this.supplierName = poCode.getSupplier().getName(); 
		}
		this.title = message.getDescription();
		this.processId = message.getProcess().getId();
		this.processName = message.getProcess().getProcessType().getProcessName();
		this.processType = message.getProcess().getProcessType().getValue();
		this.createdDate = message.getCreatedDate();
		this.userName = message.getUser().getPerson().getName();
		this.modifiedBy = message.getModifiedBy().getPerson().getName();
		this.label = message.getLabel();
	}
}
