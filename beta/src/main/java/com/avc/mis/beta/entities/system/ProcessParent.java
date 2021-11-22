/**
 * 
 */
package com.avc.mis.beta.entities.system;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.process.PoProcess;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Records relationship between used and using processes, in order to keep track of process graph.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "PROCESS_PARENTS")
public class ProcessParent extends ProcessInfoEntity {
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usedProcessId", nullable = false, updatable = false)
	private PoProcess usedProcess;
}
