/**
 * 
 */
package com.avc.mis.beta.entities.process;

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
@Table(name = "RECEIPT_ITEMS")
@PrimaryKeyJoinColumn(name = "processItemId")
public class RawItemQuality extends ProcessItem {

	@Column(scale = 3)
	private BigDecimal breakage;
	
	@Column(scale = 3)
	private BigDecimal foreignMaterial;
	
	@Column(scale = 3)
	private BigDecimal humidity;
	
	@Column(scale = 3)
	private BigDecimal testa;
	
	@Column(scale = 3)
	private BigDecimal scorched;
	
	@Column(scale = 3)
	private BigDecimal deepCut;
	
	@Column(scale = 3)
	private BigDecimal offColour;
	
	@Column(scale = 3)
	private BigDecimal shrivel;
	
	@Column(scale = 3)
	private BigDecimal desert;
	
	@Column(scale = 3)
	private BigDecimal deepSpot;
	
	@Column(scale = 3)
	private BigDecimal mold;
	
	@Column(scale = 3)
	private BigDecimal dirty;
	
	@Column(scale = 3)
	private BigDecimal decay;
	
	@Column(scale = 3)
	private BigDecimal insectDamage;
	
	@Column(scale = 3)
	private BigDecimal count;
	
	@Column(scale = 3)
	private BigDecimal smallKernels;
	
	@Column(scale = 3)
	private BigDecimal defectsAfterRoasting;
	
	@Column(scale = 3)
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
