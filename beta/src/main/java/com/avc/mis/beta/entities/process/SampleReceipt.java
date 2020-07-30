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
import com.avc.mis.beta.entities.processinfo.SampleItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "SAMPLE_RECEIPTS")
@PrimaryKeyJoinColumn(name = "processId")
public class SampleReceipt extends GeneralProcess {
	

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Sample has to contain at least one sample item")
	private Set<SampleItem> sampleItems = new HashSet<>();
	
	/**
	 * Gets the list of Items as an array (can be ordered).
	 * @return the sampleItems
	 */
	public SampleItem[] getSampleItems() {
		return this.sampleItems.toArray(new SampleItem[this.sampleItems.size()]);
	}

	/**
	 * Setter for adding items that where sampled, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param sampleItems the sampleItems to set
	 */
	public void setSampleItems(SampleItem[] sampleItems) {
		this.sampleItems = Insertable.setReferences(sampleItems, (t) -> {t.setReference(this);	return t;});
	}
	
	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Sample Cashew Receipt";
	}
}
