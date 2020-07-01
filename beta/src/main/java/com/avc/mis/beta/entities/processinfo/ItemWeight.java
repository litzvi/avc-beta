/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "ITEM_WEIGHTS")
public class ItemWeight extends ProcessEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sampleItemId", nullable = false, updatable = false)
	private SampleItem sampleItem;
	
	@Column(nullable = false, precision = 19, scale = AmountWithUnit.SCALE)
	private BigDecimal unitAmount;
		
	@Column(nullable = false)
	private BigInteger numberOfSamples;	
	
	@Column(nullable = false, precision = 19, scale = AmountWithUnit.SCALE)
	private BigDecimal avgTestedWeight;

	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof SampleItem) {
			this.setSampleItem((SampleItem)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a sample item");
		}		
	}
	
	@Override
	public boolean isLegal() {
		return unitAmount != null && numberOfSamples != null && avgTestedWeight != null 
				&& unitAmount.signum() > 0 && numberOfSamples.signum() > 0 && avgTestedWeight.signum() > 0;
	}

	@Override
	public String getIllegalMessage() {
		return "Item tested weight has to contain amountWeighed, positive number of samples and avarage weight";
	}	

}
