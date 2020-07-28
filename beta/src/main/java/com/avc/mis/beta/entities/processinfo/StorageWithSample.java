/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "STORAGE_SAMPLE")
@PrimaryKeyJoinColumn(name = "storageId")
public class StorageWithSample extends Storage {

	@Column(precision = 19, scale = MeasureUnit.SCALE)
//	@NotNull(message = "Empty container avarage weight is mandatory")
	private BigDecimal emptyContainerWeight;
	
	
//	@Column(nullable = false)
//	@NotNull(message = "Number of samples is mandetory")
	@Positive(message = "Number of samples has to be positive")
	private BigInteger numberOfSamples;	
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
//	@NotNull(message = "Average tested weight is mandetory")
	@Positive(message = "Average tested weight has to be positive")
	private BigDecimal avgTestedWeight;

}
