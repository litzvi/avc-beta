/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.entities.ProcessInfoEntity;
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
@BatchSize(size = DAO.BATCH_SIZE)
@Table(name = "USER_MESSAGES")
public class UserMessage extends ProcessInfoEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MessageLabel label;
	
	public String getLabel() {
		return this.label.name();
	}
			
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return this.getUser() != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Message needs to belong to process and user";
	}

}
