/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;

import com.avc.mis.beta.dataobjects.BankAccount;
import com.avc.mis.beta.dataobjects.PaymentAccount;
import com.avc.mis.beta.dataobjects.Phone;

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
