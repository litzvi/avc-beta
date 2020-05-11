/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Storage;
import com.avc.mis.beta.utilities.LocalDateToLong;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "PROCESSED_ITEMS")
public class ProcessItem extends ProcessInfoEntity {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	private Item item;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemPoCode", updatable = false)
	private PoCode itemPo; //should be null for receiving, for items used in the process
	
	@Column(nullable = false, scale = 3)
	private BigDecimal unitAmount = BigDecimal.ONE;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MeasureUnit measureUnit;
	
	@Column(nullable = false, scale = 3)
	private BigDecimal numberUnits;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storageLocationId")
	private Storage storageLocation;
	
//	@Convert(converter = LocalDateToLong.class)
//	private LocalDate processDate;
//	
//	public void setDeliveryDate(String processDate) {
//		if(processDate != null)
//			this.processDate = LocalDate.parse(processDate);
//	}
	
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = MeasureUnit.valueOf(measureUnit);
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof ProductionProcess) {
			this.setProcess((ProductionProcess)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a production process");
		}		
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return item != null && unitAmount != null && measureUnit != null && numberUnits != null 
				&& unitAmount.compareTo(BigDecimal.ZERO) > 0
				&& numberUnits.compareTo(BigDecimal.ZERO) > 0;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Process item specify an item, unit amount, measure unit and number of units";
	}

}
