/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.CheckStatus;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "QC_RAW_ITEMS")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@PrimaryKeyJoinColumn(name = "processItemId")
public class RawItemQuality extends ProcessInfoEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	@NotNull(message = "Item is mandatory", groups = OnPersist.class)
	private Item item;
	
	@Column(nullable = false)
	@NotNull(message = "Measure unit is mandatory")
	private MeasureUnit measureUnit;
	
	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
	@NotNull(message = "Sample weight is mandatory")
	@Positive(message = "Amount has to be positive")
	private BigDecimal sampleWeight;

	@Column(nullable = false)
	@NotNull(message = "Number of samples is mandetory")
	@Positive(message = "Number of samples has to be positive")
	private BigInteger numberOfSamples;	
	
	@Column(precision = 19, scale = 3)
	private BigInteger wholeCountPerLb;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal smallSize;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal ws;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal lp;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal breakage;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal foreignMaterial;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal humidity;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal scorched;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal deepCut;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal offColour;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal shrivel;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal desert;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal deepSpot;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal mold;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal dirty;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal lightDirty;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal decay;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal insectDamage;

	@Column(precision = 19, scale = 3)
	private BigDecimal testa;
	
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal defectsAfterRoasting;
//	
	@Column(precision = 19, scale = 3)
	private BigDecimal roastingWeightLoss;
	
	@Enumerated(EnumType.STRING)
	private CheckStatus colour; 
	
	@Enumerated(EnumType.STRING)
	private CheckStatus flavour; 
	
	public BigDecimal getTotalDefects() {
		List<BigDecimal> list = Arrays.asList(this.scorched, this.deepCut, 
				this.offColour, this.shrivel, this.desert, this.deepSpot);
		BigDecimal sum = BigDecimal.ZERO;
		for(BigDecimal augend: list) {
			if(augend != null) {
				sum = sum.add(augend);
			}
		}
		return sum;
	}
	
	public BigDecimal getTotalDamage() {
		List<BigDecimal> list = Arrays.asList(this.mold, this.dirty, 
				this.lightDirty, this.decay, this.insectDamage, this.testa);
		BigDecimal sum = BigDecimal.ZERO;
		for(BigDecimal augend: list) {
			if(augend != null) {
				sum = sum.add(augend);
			}
		}
		return sum;
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
}
