/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "EXTRA_ADDED")
@PrimaryKeyJoinColumn(name = "storageId")
public class ExtraAdded extends Storage {
	
//	public ExtraAdded() {
//		super();
//		setNumberUnits(BigDecimal.ZERO);
//	}
//
//	@Column(nullable = false, precision = 19, scale = 3)
//	private BigDecimal amount;	
//	
//	@Override
//	public boolean isLegal() {
//		return getMeasureUnit() != null && amount != null
//				&& amount.compareTo(BigDecimal.ZERO) > 0;
//	}
//
//	@Override
//	public String getIllegalMessage() {
//		return "Extra added must contain measure unit and amount";
//	}
}
