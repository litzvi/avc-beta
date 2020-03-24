/**
 * 
 */
package com.avc.mis.beta.entities;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ProcessEntity extends VersionEntity {

	@Column(updatable = false, nullable = false)
	@CreatedDate
    private Instant createdDate;
 
    @LastModifiedDate
    private Instant modifiedDate;
	

}
