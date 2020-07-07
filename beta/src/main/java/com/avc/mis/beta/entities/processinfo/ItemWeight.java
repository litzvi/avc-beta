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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.Insertable;
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
public class ItemWeight extends AuditedEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sampleItemId", nullable = false, updatable = false)
	private SampleItem sampleItem;
	
	@Column(nullable = false, precision = 19, scale = AmountWithUnit.SCALE)
	@NotNull(message = "Sampled item's unit amount is required")
	@Positive(message = "Unit amount has to be positive")
	private BigDecimal unitAmount;
		
	@Column(nullable = false)
	@NotNull(message = "Number of samples is mandetory")
	@Positive(message = "Number of samples has to be positive")
	private BigInteger numberOfSamples;	
	
	@Column(nullable = false, precision = 19, scale = AmountWithUnit.SCALE)
	@NotNull(message = "Average tested weight is mandetory")
	@Positive(message = "Average tested weight has to be positive")
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

}
