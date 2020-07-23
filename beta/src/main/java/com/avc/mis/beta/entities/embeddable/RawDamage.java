/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.avc.mis.beta.entities.process.QualityCheck;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RawDamage {

	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal mold;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal dirty;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal lightDirty;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal decay;
	
	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal insectDamage;

	@Column(precision = 19, scale = QualityCheck.SCALE)
	private BigDecimal testa;
	
	@JsonIgnore
	public BigDecimal getTotal() {
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
}
