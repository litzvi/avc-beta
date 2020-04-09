/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.entities.ProcessDataEntity;
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
@BatchSize(size = DAO.BATCH_SIZE)
@Table(name = "PROCESS_APPROVALS")
public class ApprovalTask extends ProcessDataEntity {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DecisionType decision = DecisionType.NOT_ATTENDED;
	
//	private Long processVersion;
	
	@Lob
	private String processSnapshot;
	
//	public String getDecision() {
//		return this.decision.name();
//	}
		
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
