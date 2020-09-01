/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.data.PaymentAccount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying PaymentAccount entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class PaymentAccountDTO extends SubjectDataDTO {

	private BankAccountDTO bankAccount;
	
	public PaymentAccountDTO(@NonNull PaymentAccount account) {
		super(account.getId(), account.getVersion(), account.getOrdinal());
		if(account.getBankAccount() != null)
			this.bankAccount = new BankAccountDTO(account.getBankAccount());
	}
}
