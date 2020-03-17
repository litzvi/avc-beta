/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;

import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.BankBranch;
import com.avc.mis.beta.entities.data.Phone;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class BankAccountDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private String accountNo;
	private String ownerName;
	private BankBranchDTO branch;
	
	public BankAccountDTO(@NonNull BankAccount account) {
		this.id = account.getId();
		this.accountNo = account.getAccountNo();
		this.ownerName = account.getOwnerName();
		this.branch = new BankBranchDTO(account.getBranch());
	}
}
