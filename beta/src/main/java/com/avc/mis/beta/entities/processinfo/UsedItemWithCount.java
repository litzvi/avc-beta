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


}
