/**
 * 
 */
package com.avc.mis.beta.entities.process.inventory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.dto.OrdinalAmount;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.utilities.OrdinalAmountsListToString;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Used for bags (or different kind of container),
 * who's weights are sampled to estimated the real weight.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "STORAGE_SAMPLE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@PrimaryKeyJoinColumn(name = "storageId")
public class StorageWithSample extends Storage {
	
	{
		setDtype("StorageWithSample");
	}


//	@Column(precision = 19, scale = MeasureUnit.SCALE)
//	private BigDecimal sampleContainerWeight;
	
	@Lob
	@Convert(converter = OrdinalAmountsListToString.class)
    private List<OrdinalAmount<BigDecimal>> sampleContainerWeights;
	
	@Lob
	@Convert(converter = OrdinalAmountsListToString.class)
    private List<OrdinalAmount<BigDecimal>> sampleWeights;

	@Positive(message = "Number of samples has to be positive")
	private BigInteger numberOfSamples;	
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	@Positive(message = "Average tested weight has to be positive")
	private BigDecimal avgTestedWeight;
	

}
