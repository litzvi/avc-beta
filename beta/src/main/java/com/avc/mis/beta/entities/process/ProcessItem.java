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

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Storage;
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
	@JoinColumn(name = "itemPoId", updatable = false)
	private PO itemPo;
	
	@Column(nullable = false)
	private BigDecimal unitAmount = BigDecimal.ONE;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MeasureUnit measureUnit;
	
	@Column(nullable = false)
	private BigDecimal numberUnits;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storageLocationId")
	private Storage storageLocation;	
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return super.isLegal() && item != null && unitAmount != null
				&& measureUnit != null && numberUnits != null ;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Process item needs to reference a process, "
				+ "specify an item, unit amount, measure unit and number of units";
	}

}
