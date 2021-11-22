/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO for bank branch
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankBranchDTO extends ValueDTO {
	
	private String value;
	private String bankName;
	@EqualsAndHashCode.Exclude
	private BasicValueEntity<Bank> bank;
	
	public BankBranchDTO(Integer id, String value, Integer bankId, String bankValue) {
		super(id);
		this.value = value;
		this.bankName = bankValue;
		this.bank = new BasicValueEntity<Bank>(bankId, bankValue);
	}
	
	public BankBranchDTO(@NonNull BankBranch branch) {
		super(branch.getId());
		this.value = branch.getValue();
		if(branch.getBank() != null) {
			this.bankName = branch.getBank().getValue();
			this.bank = new BasicValueEntity<Bank>(branch.getBank());
		}
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return BankBranch.class;
	}
	
	@Override
	public BankBranch fillEntity(Object entity) {
		BankBranch branchEntity;
		if(entity instanceof BankBranch) {
			branchEntity = (BankBranch) entity;
		}
		else {
			throw new IllegalStateException("Param has to be BankBranch class");
		}
		super.fillEntity(branchEntity);
		branchEntity.setValue(getValue());
		if(getBank() != null) {
			branchEntity.setBank((Bank) getBank().fillEntity(new Bank()));
		}
		return branchEntity;
	}

}
