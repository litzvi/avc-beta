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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ProcessEntity extends DataEntity {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;		

	@Column(updatable = false, nullable = false)
	@CreatedDate
    private Instant createdDate;
 
    @LastModifiedDate
    private Instant modifiedDate;    

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", updatable = false)
	private UserEntity user;
	
	private String remarks;

	
		
}
