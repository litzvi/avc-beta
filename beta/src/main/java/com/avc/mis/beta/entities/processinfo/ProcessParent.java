/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.PoProcess;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
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
