/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.MessageLabel;
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
@BatchSize(size = BaseEntity.BATCH_SIZE)
@Table(name = "USER_MESSAGES")
public class UserMessage extends ProcessInfoEntity {
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", updatable = false)
	private UserEntity user;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MessageLabel label;
	
	public String getLabel() {
		return this.label.name();
	}
			
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return super.isLegal() && this.getUser() != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Message needs to belong to process and user";
	}

}
