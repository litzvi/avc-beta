/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_FORMS")
@Inheritance(strategy=InheritanceType.JOINED)
public class Storage extends ProcessEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processItemId", nullable = false, updatable = false)
	private ProcessItem processItem;
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="unitAmount", nullable = false, precision = 19, scale = 3)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(nullable = false))
    })
	@Embedded
	private AmountWithUnit unitAmount = new AmountWithUnit(BigDecimal.ONE);
		
//	@Column(nullable = false, precision = 19, scale = 3)
//	private BigDecimal unitAmount = BigDecimal.ONE;
//	
//	@Enumerated(EnumType.STRING)
//	@Column(nullable = false)
//	private MeasureUnit measureUnit;
//	
	@Column(nullable = false, precision = 19, scale = 3)
	private BigDecimal numberUnits;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouseLocationId")
	private Warehouse warehouseLocation;
	
//	public void setMeasureUnit(String measureUnit) {
//		this.measureUnit = MeasureUnit.valueOf(measureUnit);
//	}
	
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
	
	@Override
	public boolean isLegal() {
		return unitAmount.isFilled() && numberUnits != null
				&& numberUnits.compareTo(BigDecimal.ZERO) > 0
				&& unitAmount.signum() > 0;
	}

	@Override
	public String getIllegalMessage() {
		return "Storage information must contain unit amount, measure unit and positive number of units";
	}

}
