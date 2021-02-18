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
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.GeneralInfoEntity;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A Management approval (or disapproval - perhaps should be decision task) for a process. 
 * Includes the user who approved (or needs to approve)
 * and the decision made.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESS_APPROVALS")
public class ApprovalTask extends GeneralInfoEntity {	

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", updatable = false)
	private UserEntity user;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Decision is mandatory")
	private DecisionType decision;
	
	@Lob
	private String processSnapshot;
	
}
