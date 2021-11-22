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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ObjectDataEntity;
import com.avc.mis.beta.entities.values.BankBranch;

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
public class BankAccount extends ObjectDataEntity {
	
	@Column(nullable = false)
	@NotBlank(message = "Account number is mandetory")
	private String accountNo;
	
	@Column(nullable = false)
	@NotBlank(message = "Account owner name is mandetory")
	private String ownerName;
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="branchId", nullable = false)
	@NotNull(message = "Bank acount has to have a bank branch")
	private BankBranch branch;
	
	public void setOwnerName(String ownerName) {
		this.ownerName = Optional.ofNullable(ownerName).map(s -> s.trim()).orElse(null);
	}
	
	public void setAccountNo(String accountNo) {
		this.accountNo = Optional.ofNullable(accountNo).map(s -> s.trim()).orElse(null);
	}
	

}
