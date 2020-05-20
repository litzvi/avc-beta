/**
 * 
 */
package com.avc.mis.beta.entities.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ContactEntity;
import com.avc.mis.beta.entities.Insertable;
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
@Entity
//@Where(clause = "deleted = false")
@Table(name = "PAYMENT_ACCOUNTS"/*
								 * , uniqueConstraints = { @UniqueConstraint(columnNames = { "contactId",
								 * "ordinal" }) }
								 */)
public class PaymentAccount extends ContactEntity {
	
	@JoinTable(name = "BANK_PAYEES", 
			joinColumns = @JoinColumn(name="paymentId", referencedColumnName="id"),
			inverseJoinColumns = @JoinColumn(name = "accountId",referencedColumnName = "id"))
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	private BankAccount bankAccount;
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	/**
	 * @return
	 */
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return getBankAccount() != null && getBankAccount().isLegal();
	}
	
	@Override
	public void setReference(Object referenced) {
		this.setContactDetails((ContactDetails)referenced);
		
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Payment info not legal\n "
				+ "Account has to have a legal bank account";
	}
}
