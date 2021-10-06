/**
 * 
 */
package com.avc.mis.beta.entities.plan;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.GeneralInfoEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.enums.Shift;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.utilities.LocalDateToLong;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Production Plan with bill of materials.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PRODUCTION_PLAN_ROWS")
public class ProductionPlanRow extends GeneralInfoEntity {
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId", nullable = false, updatable = false)
	@NotNull(message = "Internal error: no process type set", groups = OnPersist.class)
	private ProcessType processType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productionLineId")
	private ProductionLine productionLine;
	
	@Enumerated(EnumType.STRING)
	private Shift shift; 

	private Integer numOfWorkers;

	
	@Column(nullable = false)
	@NotNull(message = "Planned date is mandatory")
	@Convert(converter = LocalDateToLong.class)
	private LocalDate plannedDate;
	
	@Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "plan", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.ALL, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Production Plan has to have at least one process item plan line")
	private Set<ProcessItemPlan> processItemPlans = new HashSet<>();
	
	@Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "plan", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.ALL, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Production Plan has to have at least one used item plan line")
	private Set<UsedItemPlan> usedItemPlans = new HashSet<>();

	public void setProcessItemPlans(Set<ProcessItemPlan> processItemPlans) {
		this.processItemPlans = Insertable.setReferences(processItemPlans, (t) -> {t.setReference(this);	return t;});
	}
	
	public void setUsedItemPlans(Set<UsedItemPlan> usedItemPlans) {
		this.usedItemPlans = Insertable.setReferences(usedItemPlans, (t) -> {t.setReference(this);	return t;});
	}
		
}
