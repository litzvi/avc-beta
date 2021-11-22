/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.BankAccount;
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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentAccountDTO extends SubjectDataDTO {

	private BankAccountDTO bankAccount;
	
	public PaymentAccountDTO(@NonNull PaymentAccount account) {
		super(account.getId(), account.getVersion(), account.getOrdinal());
		if(account.getBankAccount() != null)
			this.bankAccount = new BankAccountDTO(account.getBankAccount());
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return PaymentAccount.class;
	}
	
	@Override
	public PaymentAccount fillEntity(Object entity) {
		PaymentAccount paEntity;
		if(entity instanceof PaymentAccount) {
			paEntity = (PaymentAccount) entity;
		}
		else {
			throw new IllegalStateException("Param has to be PaymentAccount class");
		}
		super.fillEntity(paEntity);
		if(getBankAccount() != null) {
			paEntity.setBankAccount(getBankAccount().fillEntity(new BankAccount()));
		}
		
		return paEntity;
	}

}
