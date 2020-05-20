/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "SAMPLE_WEIGHTS")
public class SampleItem extends ProcessInfoEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	private Item item;
	
	@Column(nullable = false, scale = 3)
	private BigDecimal unitAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MeasureUnit measureUnit;
	
	@Column(nullable = false, scale = 3)
	private BigDecimal numberOfSamples;	
	
	@Column(nullable = false, scale = 3)
	private BigDecimal avgTestedWeight;	
	
	@Column(nullable = false, scale = 3)
	private BigDecimal emptyContainerWeight;	
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return item != null && unitAmount != null && measureUnit != null && numberOfSamples != null 
				&& avgTestedWeight != null && emptyContainerWeight != null
				&& unitAmount.compareTo(BigDecimal.ZERO) > 0
				&& numberOfSamples.compareTo(BigDecimal.ZERO) > 0;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Sample weight must specify an item, unit amount, measure unit "
				+ "number of samples avarage tested weight and empty container weight.";
	}

}
