/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.time.Instant;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.UserMessage;
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
public class UserMessageDTO extends DataDTO {

	private String poCode;
	private String title;
	private Integer processId;
	private String processType;
	private Instant createdDate;
	private String userName;
	private String modifiedBy;
	private String label;
	private String remarks;
	
	public UserMessageDTO(Integer id, Integer version, PoCode poCode, String title, Integer processId, ProcessType processType, 
			Instant createdDate, String userName, String modifiedBy, MessageLabel label) {
		super(id, version);
		this.title = title;
		this.processId = processId;
		this.poCode = poCode.getValue();
		this.processType = processType.getValue();
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
		this.poCode = message.getProcess().getPoCode().getValue();
		this.processType = message.getProcess().getProcessType().getValue();
//		this.createdDate = LocalDateTime.ofInstant(message.getCreatedDate(), ZoneOffset.UTC);
		this.userName = message.getUser().getPerson().getName();
		this.modifiedBy = message.getModifiedBy().getPerson().getName();
		this.createdDate = message.getCreatedDate();
		this.label = message.getLabel();
	}
}
