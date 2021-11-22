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

import org.hibernate.annotations.Where;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.process.group.ProcessGroup;
import com.avc.mis.beta.entities.process.group.ProcessItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * An abstract po process that produces items - has process items.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "PROCESSES_WITH_PRODUCT")
@PrimaryKeyJoinColumn(name = "processId")
public abstract class ProcessWithProduct<T extends ProcessItem> extends PoProcess {

	@OneToMany(mappedBy = "process", targetEntity = ProcessGroup.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@Where(clause = "dtype = 'ProcessItem'")
	private Set<ProcessItem> processItems = new HashSet<>();

	/**
	 * Setter for adding items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param processItems the processItems to set
	 */
	protected void setProcessItems(Set<T> processItems) {
		this.processItems = (Set<ProcessItem>) Insertable.setReferences(processItems, (t) -> {t.setReference(this);	return t;});
	}
}
