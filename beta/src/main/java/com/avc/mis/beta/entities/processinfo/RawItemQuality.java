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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.enums.CheckStatus;

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
@PrimaryKeyJoinColumn(name = "processItemId")
public class RawItemQuality extends ProcessItem {

	@Column(precision = 19, scale = 3)
	private BigDecimal breakage;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal foreignMaterial;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal humidity;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal testa;
	
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
	private BigDecimal count;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal smallKernels;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal defectsAfterRoasting;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal weightLoss;
	
	@Enumerated(EnumType.STRING)
	private CheckStatus colour; 
	
	@Enumerated(EnumType.STRING)
	private CheckStatus flavour; 
	
	public BigDecimal getTotalDefects() {
		List<BigDecimal> list = Arrays.asList(this.testa, this.scorched, this.deepCut, 
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
		List<BigDecimal> list = Arrays.asList(this.mold, this.dirty, this.decay, this.insectDamage);
		BigDecimal sum = BigDecimal.ZERO;
		for(BigDecimal augend: list) {
			if(augend != null) {
				sum = sum.add(augend);
			}
		}
		return sum;
	}
}
