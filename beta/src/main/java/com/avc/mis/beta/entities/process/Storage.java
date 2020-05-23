/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_FORMS")
public class Storage extends ProcessEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processItemId", nullable = false, updatable = false)
	private ProcessItem processItem;
		
	@Column(nullable = false, scale = 3)
	private BigDecimal unitAmount = BigDecimal.ONE;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MeasureUnit measureUnit;
	
	@Column(nullable = false, scale = 3)
	private BigDecimal numberUnits;	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "warehouseLocationId")
	private Warehouse warehouseLocation;
	
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = MeasureUnit.valueOf(measureUnit);
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
	
	@Override
	public boolean isLegal() {
		return unitAmount != null && measureUnit != null && numberUnits != null;
	}

	@Override
	public String getIllegalMessage() {
		return "Storage information must contain unit amount, measure unit and number of units";
	}

}
