/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.avc.mis.beta.entities.values.BankBranch;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true ,callSuper = true)
@Entity
@Table(name = "BANK_ACCOUNTS")
public class BankAccount extends ObjectEntityWithId {
	
	@Column(nullable = false)
	private String accountNo;
	
	@Column(nullable = false)
	private String ownerName;
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="branchId", nullable = false)
	private BankBranch branch;
	
	public void setOwnerName(String ownerName) {
		this.ownerName = Optional.ofNullable(ownerName).map(s -> s.trim()).orElse(null);
	}
	
	public void setAccountNo(String accountNo) {
		this.accountNo = Optional.ofNullable(accountNo).map(s -> s.trim()).orElse(null);
	}
	
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	

	/**
	 * @return
	 */
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getAccountNo()) && 
				StringUtils.isNotBlank(getOwnerName()) && getBranch() != null;
	}
	
	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Bank Account info not legal\n "
				+ "account has to belong to bank branch,\n"
				+ "account number and Owner name can't be blank";
	}

}
