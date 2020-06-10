/**
 * 
 */
package com.avc.mis.beta.entities.values;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ValueEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
//@BatchSize(size = BaseEntity.BATCH_SIZE)
@Entity
@Table(name="BANK_BRANCHES")
public class BankBranch extends ValueEntity {
	
	@Column(name = "name", nullable = false)
	private String value;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bankId", nullable = false)
	private Bank bank;
	
	public void setValue(String value) {
		this.value = value.trim();
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof Country) {
			this.setBank((Bank)referenced);
		}
		else {
			throw new ClassCastException("Branch needs to have a bank, bank not set");
		}		
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue()) && getBank() != null;
	}
	
	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Branch name can't be blank and branch has to belong to a bank";
	}
}
