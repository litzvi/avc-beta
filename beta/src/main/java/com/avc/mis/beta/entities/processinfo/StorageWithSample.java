/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.utilities.MapToString;

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
@PrimaryKeyJoinColumn(name = "storageFormId")
public class StorageWithSample extends Storage {

	@Column(precision = 19, scale = MeasureUnit.SCALE)
	private BigDecimal sampleContainerWeight;	
	
	@Positive(message = "Number of samples has to be positive")
	private BigInteger numberOfSamples;	
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	@Positive(message = "Average tested weight has to be positive")
	private BigDecimal avgTestedWeight;
	
//	@Lob
//	private String sampleDetails;
	
	@Convert(converter = MapToString.class)
    private Map<Integer, BigDecimal> sampleWeights;


	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
//	@Override
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
}
