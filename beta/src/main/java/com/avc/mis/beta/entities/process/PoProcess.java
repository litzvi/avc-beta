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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.process.collection.ItemCount;
import com.avc.mis.beta.entities.process.collection.ProcessParent;
import com.avc.mis.beta.entities.process.collection.WeightedPo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Process that also refers to a specific single PO#
 * 
 * @author Zvi
 * 
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "PO_PROCESSES")
@PrimaryKeyJoinColumn(name = "processId")
public abstract class PoProcess extends GeneralProcess {	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, name = "po_code_code")
	private BasePoCode poCode;
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", 
		cascade = {CascadeType.REMOVE}, 
		fetch = FetchType.LAZY)
	private Set<WeightedPo> weightedPos = new HashSet<>();
	
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "process", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<ProcessParent> processParents = new HashSet<>();
	
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "usedProcess", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<ProcessParent> processChildren;
		
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<ItemCount> itemCounts = new HashSet<>();

	public WeightedPo[] getWeightedPos() {
		if(this.weightedPos == null)
			return null;
		WeightedPo[] weightedPos = this.weightedPos.toArray(new WeightedPo[this.weightedPos.size()]);
		Arrays.sort(weightedPos, Ordinal.ordinalComparator());
		return weightedPos;
	}

	public void setWeightedPos(WeightedPo[] weightedPos) {
		Ordinal.setOrdinals(weightedPos);
		this.weightedPos = Insertable.setReferences(weightedPos, (t) -> {t.setReference(this);	return t;});
	}

	
	/**
	 * Gets the list of Item counts as an array (can be ordered).
	 * @return the itemCounts
	 */
	public ItemCount[] getItemCounts() {
		if(this.itemCounts == null || this.itemCounts.isEmpty())
			return null;
		ItemCount[] itemCounts = this.itemCounts.toArray(new ItemCount[this.itemCounts.size()]);
		Arrays.sort(itemCounts, Ordinal.ordinalComparator());
		return itemCounts;
	}

	/**
	 * Setter for adding item counts, 
	 * receives an array (which can be ordered, for later use to add an order to the item counts).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param itemCounts the itemCounts to set
	 */
	public void setItemCounts(ItemCount[] itemCounts) {
		Ordinal.setOrdinals(itemCounts);
		this.itemCounts = Insertable.setReferences(itemCounts, (t) -> {t.setReference(this);	return t;});
	}
	
}
