/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.enums.QcCompany;
import com.avc.mis.beta.entities.process.collectionItems.CashewItemQuality;
import com.avc.mis.beta.entities.process.group.ProcessItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class QualityCheck extends ProcessWithProduct<ProcessItem> {
	/**
	 * Decimal scale for QC results
	 */
	public static final int SCALE = 4;	
	
	@Column(nullable = false)
	@NotNull(message = "Checked by is mandatory")
	private QcCompany checkedBy;

	
	private String inspector;
	private String sampleTaker;
	
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Quality check has to contain at least one testsed item")
	private Set<CashewItemQuality> testedItems = new HashSet<>();
	
	public void setCheckedBy(String checkedBy) {
		this.checkedBy = QcCompany.valueOfLabel(checkedBy);
	}
	
	public String getCheckedBy() {
		if(this.checkedBy != null)
			return this.checkedBy.toString();
		return null;
	}
	
	/**
	 * Setter for adding items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param processItems the processItems to set
	 */
	public void setProcessItems(Set<ProcessItem> processItems) {
		super.setProcessItems(processItems);
	}

	/**
	 * Setter for adding items that where tested, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param testedItems the testedItems to set
	 */
	public void setTestedItems(Set<CashewItemQuality> testedItems) {
		this.testedItems = Insertable.setReferences(testedItems, (t) -> {t.setReference(this);	return t;});
	}
	
	@NotNull(message = "QC has to reference a po code")
	@Override
	public BasePoCode getPoCode() {
		return super.getPoCode();
	}

}