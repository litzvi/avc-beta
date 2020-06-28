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

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.processinfo.RawItemQuality;
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
@Table(name = "QC_TESTS")
@PrimaryKeyJoinColumn(name = "processId")
public class QualityCheck extends ProductionProcess {
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<RawItemQuality> testedItems = new HashSet<>();
	
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "itemId", updatable = false, nullable = false)
//	private Item item;
//	
//	@Column(nullable = false)
//	private Integer numOfSamples;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal sampleSize;	
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "storageLocationId")
//	private Warehouse storageLocation;
	
	/**
	 * Gets the list of Items as an array (can be ordered).
	 * @return the sampleItems QC info for raw cashew
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
	
	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public boolean isLegal() {
		return super.isLegal() && getTestedItems().length > 0;
	}

	@Override
	public String getIllegalMessage() {
		return super.getIllegalMessage() + " or no items tested ";
	}

	@Override
	public String getProcessTypeDescription() {
		return "Quality Check";
	}
	

}