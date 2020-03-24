/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.values.BankBranch;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class BankBranchDTO extends ValueDTO {
	
	String value;
	String bankName;
	
	public BankBranchDTO(Integer id, String value, String bankName) {
		super(id);
		this.value = value;
		this.bankName = bankName;
	}
	
	public BankBranchDTO(@NonNull BankBranch branch) {
		super(branch.getId());
		this.value = branch.getValue();
		this.bankName = branch.getBank().getValue();
	}
}
