/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "SAMPLES")
@Inheritance(strategy=InheritanceType.JOINED)
public class Sample extends AuditedEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processItemId", nullable = false, updatable = false)
	@NotNull
	private ProcessItem processItem;
		
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Measure unit required")
	private MeasureUnit measureUnit;
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	private BigDecimal containerWeight;	
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "sample", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Sample has to contain at least one sampled amount")
	private Set<SampledAmount> amounts;
	
	public SampledAmount[] getAmounts() {
		return this.amounts.toArray(new SampledAmount[this.amounts.size()]);
	}
	
	public void setAmounts(SampledAmount[] amounts) {
		Ordinal.setOrdinals(amounts);
		this.amounts = Insertable.setReferences(amounts, (t) -> {t.setReference(this);	return t;});
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof ProcessItem) {
			this.setProcessItem((ProcessItem)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a process item");
		}		
	}

}
