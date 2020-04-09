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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.avc.mis.beta.entities.data.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 * Abstract class for data entities that represent process recording that record auditing data.
 * e.g. users who created/modified, corresponding dates and optional remarks.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ProcessEntity extends DataEntity {
	
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
	
	private String remarks;

	
		
}
