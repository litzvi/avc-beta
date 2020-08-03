/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.processinfo.UserMessage;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
	@JoinColumn(updatable = false, nullable = false)
	private PoCode poCode;	
	
	
}
