/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "SAMPLE_AMOUNTS")
@Inheritance(strategy=InheritanceType.JOINED)
public class SampledAmount extends AuditedEntity implements Ordinal {

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sampleId", nullable = false, updatable = false)
	@NotNull
	private Sample sample;
	
	@Column(nullable = false)
	private Integer ordinal;
	
	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
	@NotNull(message = "Amount is required")
	@Positive(message = "Amount has to be positive")
	private BigDecimal amount;
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof Sample) {
			this.setSample((Sample)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a sample");
		}		
	}
}
