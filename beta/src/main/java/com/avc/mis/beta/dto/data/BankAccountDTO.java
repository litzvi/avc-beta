/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.values.BankBranch;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying BankAccount entity data.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankAccountDTO extends DataDTO {

	private String accountNo;
	private String ownerName;
	private BankBranchDTO branch;
	
	public BankAccountDTO(Integer id, Integer version, 
			String accountNo, String ownerName, 
			Integer branchId, String branchValue, Integer bankId, String bankValue) {
		super(id, version);
		this.accountNo = accountNo;
		this.ownerName = ownerName;
		this.branch = new BankBranchDTO(branchId, branchValue, bankId, bankValue);
	}
	
	public BankAccountDTO(@NonNull BankAccount account) {
		super(account.getId(), account.getVersion());
		this.accountNo = account.getAccountNo();
		this.ownerName = account.getOwnerName();
		this.branch = new BankBranchDTO(account.getBranch());
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return BankAccount.class;
	}
	
	@Override
	public BankAccount fillEntity(Object entity) {
		BankAccount baEntity;
		if(entity instanceof BankAccount) {
			baEntity = (BankAccount) entity;
		}
		else {
			throw new IllegalStateException("Param has to be BankAccount class");
		}
		super.fillEntity(baEntity);
		baEntity.setAccountNo(getAccountNo());
		baEntity.setOwnerName(getOwnerName());
		if(getBranch() != null) {
			baEntity.setBranch(getBranch().fillEntity(new BankBranch()));
		}
		return baEntity;
	}

}
