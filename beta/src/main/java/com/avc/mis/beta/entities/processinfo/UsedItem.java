package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "USED_ITEMS")
public class UsedItem extends ProcessInfoEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storageId")
	private Storage storage;
	
	@Column(nullable = false, precision = 19, scale = AmountWithUnit.SCALE)
	private BigDecimal numberUnits;	

	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	

	@Override
	public boolean isLegal() {
		return storage != null && numberUnits != null
				&& numberUnits.signum() > 0;
	}

	@Override
	public String getIllegalMessage() {
		return "Used item must reference a storage and have positive number of units";
	}

}
