package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	
	@OneToOne()
	private Storage storage;
	
	@Column(nullable = false, precision = 19, scale = AmountWithUnit.SCALE)
	private BigDecimal numberUnits;	


	@Override
	public String getIllegalMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
