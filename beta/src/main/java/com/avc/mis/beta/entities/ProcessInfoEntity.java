/**
 * 
 */
package com.avc.mis.beta.entities;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class for entities representing information notifying about a process entity.
 * e.g. message about a process transaction, task required for process. 
 * References a process (can't be changed) and has a title.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ProcessInfoEntity extends DataEntity {

	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;		

	@JsonIgnore
	@Column(updatable = false, nullable = false)
	@CreatedDate
    private Instant createdDate;
 
	@JsonIgnore
    @LastModifiedDate
    private Instant modifiedDate;    

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", updatable = false)
	private UserEntity user;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userModifingId")
	@LastModifiedBy
	private UserEntity modifiedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", nullable = false, updatable = false)
	private ProductionProcess process;
	
	private String title;

	private String remarks;
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return this.process != null;
	}
	
	@PrePersist
	@Override
	public void prePersist() {
		if(!isLegal() && this.process != null)
			throw new IllegalArgumentException(this.getIllegalMessage());
	}
	
}
