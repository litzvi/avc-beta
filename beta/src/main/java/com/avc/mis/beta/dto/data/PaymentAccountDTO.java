/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.io.Serializable;

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
@NoArgsConstructor
public class PaymentAccountDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private BankAccountDTO bankAccount;
	
	public PaymentAccountDTO(@NonNull PaymentAccount account) {
		this.id = account.getId();
		if(account.getBankAccount() != null)
			this.bankAccount = new BankAccountDTO(account.getBankAccount());
	}
}
