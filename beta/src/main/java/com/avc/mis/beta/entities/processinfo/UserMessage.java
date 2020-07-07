/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.GeneralInfoEntity;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
//@BatchSize(size = BaseEntity.BATCH_SIZE)
@Table(name = "USER_MESSAGES")
public class UserMessage extends GeneralInfoEntity {
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", updatable = false)
	@NotNull(message = "Message needs to belong to user", groups = OnPersist.class)
	private UserEntity user;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MessageLabel label;
	
	public String getLabel() {
		return this.label.name();
	}
	
}
