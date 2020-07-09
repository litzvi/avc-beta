/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.CheckStatus;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.avc.mis.beta.validation.groups.PositiveAmount;

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
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="sampleAmount", nullable = false, 
                           	precision = 19, scale = AmountWithUnit.SCALE)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(nullable = false))
    })
	@Embedded
	@NotNull(message = "Sample amount is mandatory")
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit sampleWeight;

	
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
		List<BigDecimal> list = Arrays.asList(this.mold, this.dirty, this.decay, this.insectDamage, this.testa);
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
