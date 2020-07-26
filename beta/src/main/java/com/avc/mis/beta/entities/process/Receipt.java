/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.entities.processinfo.SampleItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "ORDER_RECEIPTS")
@PrimaryKeyJoinColumn(name = "processId")
public class Receipt extends ProductionProcess {
	

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<SampleItem> sampleItems = new HashSet<>();
	
	public void setReceiptItems(ReceiptItem[] receiptItems) {
		super.setProcessItems(receiptItems);
	}
	
	public ReceiptItem[] getReceiptItems() {
		ProcessItem[] processItems = getProcessItems();
		return Arrays.copyOf(processItems, processItems.length, ReceiptItem[].class);
	}
	
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
	
	@PrePersist
	@Override
	public void prePersist() {
		super.prePersist();
		//perhaps general order should be final
		getLifeCycle().setProcessStatus(ProcessStatus.PENDING);
		if(getProcessItems().length == 0)
			throw new IllegalArgumentException("Receipt has to containe at least one item line");
		
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Receipt";
	}
	
}
