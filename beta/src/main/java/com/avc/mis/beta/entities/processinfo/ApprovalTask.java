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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
//@BatchSize(size = BaseEntity.BATCH_SIZE)
@Table(name = "PROCESS_APPROVALS")
public class ApprovalTask extends ProcessInfoEntity {	

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", updatable = false)
	private UserEntity user;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DecisionType decision = DecisionType.NOT_ATTENDED;
	
	@Lob
	private String processSnapshot;
		
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return super.isLegal() && this.decision != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Process approval needs to reference a process and have a decision";
	}
}
