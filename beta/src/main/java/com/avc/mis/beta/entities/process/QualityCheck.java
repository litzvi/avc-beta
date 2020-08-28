/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.RawItemQuality;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Quality Check for received raw cashew.
 * May have process items but not mandatory. e.g. store the samples taken for QC process.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "QC_TESTS")
@PrimaryKeyJoinColumn(name = "processId")
public class QualityCheck extends PoProcess {
	/**
	 * Decimal scale for QC results
	 */
	public static final int SCALE = 4;	
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<ProcessItem> processItems = new HashSet<>();
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Quality check has to contain at least one testsed item")
	private Set<RawItemQuality> testedItems = new HashSet<>();
	
	/**
	 * Gets the list of Items of QC process items as an array (can be ordered).
	 * @return the processItems
	 */
	public ProcessItem[] getProcessItems() {
		return this.processItems.toArray(new ProcessItem[this.processItems.size()]);
	}

	/**
	 * Setter for adding items stored after using for QC
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the null items and set needed references to satisfy needed foreign keys of database.
	 * @param processItems the processItems to set
	 */
	public void setProcessItems(ProcessItem[] processItems) {
		this.processItems = Insertable.setReferences(processItems, (t) -> {t.setReference(this);	return t;});
	}

	/**
	 * Gets the list of raw QC results as an array (can be ordered).
	 * @return array of RawItemQuality QC info for raw cashew
	 */
	public RawItemQuality[] getTestedItems() {
		return this.testedItems.toArray(new RawItemQuality[this.testedItems.size()]);
	}

	/**
	 * Setter for adding items that where tested, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param testedItems the testedItems to set
	 */
	public void setTestedItems(RawItemQuality[] testedItems) {
		this.testedItems = Insertable.setReferences(testedItems, (t) -> {t.setReference(this);	return t;});
	}
	
	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Quality Check";
	}
	

}