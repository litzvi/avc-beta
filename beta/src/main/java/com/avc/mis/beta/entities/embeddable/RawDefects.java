/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
public class RawDefects {

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
	
	@JsonIgnore
	public BigDecimal getTotal() {
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
}
