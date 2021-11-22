/**
 * 
 */
package com.avc.mis.beta.utilities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.SequenceIdentifier;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for keeping runing index/code for unique auto generation needs
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(indexes = @Index(columnList = "identifier", unique = true))
public class ProgramSequence extends BaseEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false, unique = true)
	private SequenceIdentifier identifier;
	
	private int sequance = 1;

	public void advance() {
		this.sequance++;
	}
}
