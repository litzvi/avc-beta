/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Process that refers to a certain PO#
 * 
 * @author Zvi
 * 
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PO_PROCESSES")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class PoProcess extends GeneralProcess {	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private PoCode poCode;	
	
	
}
