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

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.data.ProcessTypeAlert;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "name", unique = true, nullable = false)
	private ProcessName value;
	
//	@JoinTable(name = "APPROVAL_REQUIRMENTS",
//			joinColumns = @JoinColumn(name = "processTypeId", referencedColumnName = "id"), 
//			inverseJoinColumns = @JoinColumn(name = "staffId", referencedColumnName = "personId"))
	@OneToMany(mappedBy = "processType", fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<ProcessTypeAlert> alertRequirments = new HashSet<>();
	
	public String getValue() {
		return value.name();
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return value != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Process type value can't be blank";
	}

}
