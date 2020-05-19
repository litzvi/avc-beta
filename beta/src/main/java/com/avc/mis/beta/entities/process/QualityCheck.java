/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Storage;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "itemId", updatable = false, nullable = false)
//	private Item item;
//	
//	@Column(nullable = false)
//	private Integer numOfSamples;
//	
//	@Column(scale = 3)
//	private BigDecimal sampleSize;	
//	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "storageLocationId")
//	private Storage storageLocation;
	
	public void setProcessItems(RawItemQuality[] qualityChecks) {
		super.setProcessItems(qualityChecks);
	}
	
	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public boolean isLegal() {
		return super.isLegal() && getProcessItems().length > 0;
	}

	@Override
	public String getIllegalMessage() {
		return super.getIllegalMessage() + " or no items checked ";
	}
	

}