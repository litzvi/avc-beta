/**
 * 
 */
package com.avc.mis.beta.entities.process.collection;

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
import javax.validation.constraints.PositiveOrZero;

import com.avc.mis.beta.entities.RankedAuditedEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;

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
@Deprecated
public class ItemWeight extends RankedAuditedEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sampleItemId", nullable = false, updatable = false)
	private SampleItem sampleItem;
	
	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
	@NotNull(message = "Sampled item's unit amount is required")
	@Positive(message = "Unit amount has to be positive")
	private BigDecimal unitAmount;
	
	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
	@NotNull(message = "Number of units is required")
	@Positive(message = "Number of units has to be positive")
	private BigDecimal numberUnits;
		
//	@Column(nullable = false)
//	@NotNull(message = "Number of samples is mandetory")
	@PositiveOrZero(message = "Number of samples has to be positive or zero")
	private BigInteger numberOfSamples;	
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
//	@NotNull(message = "Average tested weight is mandetory")
	@Positive(message = "Average tested weight has to be positive")
	private BigDecimal avgTestedWeight;
	
	
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
