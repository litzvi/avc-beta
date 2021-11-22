/**
 * 
 */
package com.avc.mis.beta.entities.values;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ValueEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Branch of a bank entity
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="BANK_BRANCHES")
public class BankBranch extends ValueEntity {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bankId", nullable = false)
	@NotNull(message = "Branch has to belong to a bank")
	private Bank bank;
	
	@Override
	public Bank getReference() {
		return getBank();
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof Bank) {
			this.setBank((Bank)referenced);
		}
		else {
			throw new ClassCastException("Branch needs to have a bank, bank not set");
		}		
	}
	
}
