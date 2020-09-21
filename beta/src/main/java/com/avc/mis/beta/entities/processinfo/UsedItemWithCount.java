/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "USED_ITEM_COUNTS")
public class UsedItemWithCount extends UsedItem {

	@Column(precision = 19, scale = MeasureUnit.SCALE)
	private BigDecimal containerWeight;	
	
	@Positive(message = "Number of samples has to be positive")
	private BigInteger numberOfSamples = BigInteger.ONE;	
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	@Positive(message = "Average tested weight has to be positive")
	private BigDecimal avgTestedWeight;

	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
	@Override
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
}
