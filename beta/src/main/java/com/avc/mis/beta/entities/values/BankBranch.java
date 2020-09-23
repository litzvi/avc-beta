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
import javax.validation.constraints.NotBlank;
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
	
	@Column(name = "name", nullable = false)
	@NotBlank(message = "Branch name(value) is mandatory")
	private String value;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bankId", nullable = false)
	@NotNull(message = "Branch has to belong to a bank")
	private Bank bank;
	
	public void setValue(String value) {
		this.value = value.trim();
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
	
	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
}
