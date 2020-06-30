/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.time.Instant;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.processinfo.UserMessage;

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
public class UserMessageDTO extends DataDTO {

	private PoCodeBasic poCode;
	private String title;
	private Integer processId;
	private ProcessName processName; 
	private Instant createdDate;
	private String userName;
	private String modifiedBy;
	private String label;
	private String remarks;
	
	public UserMessageDTO(Integer id, Integer version, 
			Integer poCodeId, ContractTypeCode contractTypeCode, 
			String title, Integer processId, ProcessName processName, 
			Instant createdDate, String userName, String modifiedBy, MessageLabel label) {
		super(id, version);
		this.title = title;
		this.processId = processId;
		if(poCodeId != null) {
			this.poCode = new PoCodeBasic(poCodeId, contractTypeCode);
		}
		this.processName = processName;
//		this.createdDate = LocalDateTime.ofInstant(createdDate, ZoneOffset.UTC);
		this.userName = userName;
		this.modifiedBy = modifiedBy;
		this.createdDate = createdDate;
		this.label = label.name();
	}
	
	public UserMessageDTO(@NonNull UserMessage message) {
		super(message.getId(), message.getVersion());
		this.title = message.getDescription();
		this.processId = message.getProcess().getId();
		this.poCode = new PoCodeBasic(message.getProcess().getPoCode());
		this.processName = message.getProcess().getProcessType().getProcessName();
//		this.createdDate = LocalDateTime.ofInstant(message.getCreatedDate(), ZoneOffset.UTC);
		this.userName = message.getUser().getPerson().getName();
		this.modifiedBy = message.getModifiedBy().getPerson().getName();
		this.createdDate = message.getCreatedDate();
		this.label = message.getLabel();
	}
}
