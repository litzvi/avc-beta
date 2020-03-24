/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.entities.data.BankAccount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountDTO extends DataDTO {

	private String accountNo;
	private String ownerName;
	private BankBranchDTO branch;
	
	public BankAccountDTO(@NonNull BankAccount account) {
		super(account.getId(), account.getVersion());
		this.accountNo = account.getAccountNo();
		this.ownerName = account.getOwnerName();
		this.branch = new BankBranchDTO(account.getBranch());
	}
}
