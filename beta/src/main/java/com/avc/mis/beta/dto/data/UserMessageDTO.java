/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.enums.MessageLabel;
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

	private String title;
	private Integer processId;
	private String processType;
	private Instant createdDate;
	private String label;
	private String remarks;
	
	public UserMessageDTO(Integer id, Long version, String title, Integer processId, ProcessType processType, 
			Instant createdDate, MessageLabel label) {
		super(id, version);
		this.title = title;
		this.processId = processId;
		this.processType = processType.getValue();
//		this.createdDate = LocalDateTime.ofInstant(createdDate, ZoneOffset.UTC);
		this.createdDate = createdDate;
		this.label = label.name();
	}
	
	public UserMessageDTO(@NonNull UserMessage message) {
		super(message.getId(), message.getVersion());
		this.title = message.getTitle();
		this.processId = message.getProcess().getId();
		this.processType = message.getProcess().getProcessType().getValue();
//		this.createdDate = LocalDateTime.ofInstant(message.getCreatedDate(), ZoneOffset.UTC);
		this.createdDate = message.getCreatedDate();
		this.label = message.getLabel();
	}
}
