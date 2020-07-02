/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
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

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.enums.CheckStatus;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private Item item;
	
	
	@Column(precision = 19, scale = 3)
	private BigDecimal wholeCountPerLb;
	
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
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return item != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Raw item QC has to reference an item";
	}

}
