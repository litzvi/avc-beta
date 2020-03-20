/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.BaseDTOWithVersion;
import com.avc.mis.beta.entities.data.PaymentAccount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class PaymentAccountDTO extends BaseDTOWithVersion {
//	@EqualsAndHashCode.Exclude
//	private Integer id;
	private BankAccountDTO bankAccount;
	
	public PaymentAccountDTO(@NonNull PaymentAccount account) {
		super(account.getId(), account.getVersion());
		if(account.getBankAccount() != null)
			this.bankAccount = new BankAccountDTO(account.getBankAccount());
	}
}
