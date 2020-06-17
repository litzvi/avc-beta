/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="PROCESS_TYPES")
public class ProcessType extends ValueEntity {

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(name = "name", unique = true, nullable = false)
	private ProcessName processName;
	
//	@JoinTable(name = "APPROVAL_REQUIRMENTS",
//			joinColumns = @JoinColumn(name = "processTypeId", referencedColumnName = "id"), 
//			inverseJoinColumns = @JoinColumn(name = "staffId", referencedColumnName = "personId"))
	@JsonIgnore
	@OneToMany(mappedBy = "processType", fetch = FetchType.LAZY)
//	@BatchSize(size = BaseEntity.BATCH_SIZE)
	private Set<ProcessManagement> alertRequirments = new HashSet<>();
	
	public String getValue() {
		return processName.name();
	}
	
	@Override
	public String toString() {
		return getValue();
		
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return processName != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Process type has to have a unique value";
	}

}
