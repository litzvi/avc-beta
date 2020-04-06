/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.data.UserMessage;
import com.avc.mis.beta.entities.values.ProcessType;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserMessageDTO extends ValueDTO {

	String title;
	String processType;
	LocalDateTime createdDate;
	
	public UserMessageDTO(Integer id, String title, ProcessType processType, Instant createdDate) {
		super(id);
		this.title = title;
		this.processType = processType.getValue();
		this.createdDate = LocalDateTime.ofInstant(createdDate, ZoneOffset.UTC);
	}
	
	public UserMessageDTO(@NonNull UserMessage message) {
		super(message.getId());
		this.title = message.getTitle();
		this.processType = message.getProcess().getProcessType().getValue();
		this.createdDate = LocalDateTime.ofInstant(message.getCreatedDate(), ZoneOffset.UTC);
	}
}
