/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.enums.CheckStatus;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Quality Check measurements taken when receiving raw kernel cashew.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "QC_RAW_ITEMS")
public class RawItemQuality extends ProcessInfoEntity {
	
	@Column(nullable = false, updatable = false)
	private boolean precentage = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	@NotNull(message = "Item is mandatory", groups = OnPersist.class)
	private Item item;
	
	@Column(nullable = false)
	@NotNull(message = "Measure unit is mandatory")
	private MeasureUnit measureUnit;
	
	@Column(nullable = false, precision = 19, scale = QualityCheck.SCALE)
	@NotNull(message = "Sample weight is mandatory")
	@Positive(message = "Amount has to be positive")
	private BigDecimal sampleWeight;

	@Positive(message = "Number of samples has to be positive")
	private BigInteger numberOfSamples;	
	
	private BigInteger wholeCountPerLb;
	
	private BigInteger smallSize;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal ws;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal lp;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal breakage;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal foreignMaterial;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal humidity;
	
	@NotNull
	@Embedded
	private RawDefects defects;

	@NotNull
	@Embedded
	private RawDamage damage;
		
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal roastingWeightLoss;
	
	@Enumerated(EnumType.STRING)
	private CheckStatus colour; 
	
	@Enumerated(EnumType.STRING)
	private CheckStatus flavour; 
	
	/**
	 * @return total of raw defects
	 */
	public BigDecimal getTotalDefects() {
		return this.defects.getTotal();
	}
	
	/**
	 * @return total of raw damage
	 */
	public BigDecimal getTotalDamage() {
		return this.damage.getTotal();
	}
	
	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
}
