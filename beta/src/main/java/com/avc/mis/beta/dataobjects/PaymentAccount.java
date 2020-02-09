/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class PaymentAccount {
	private int id;
	private int contactId;
	
	private BankAccount bankAccount;
	
	/**
	 * @param jdbcTemplateObject
	 * @param paymentAccounts
	 */
	public static void insertPaymentAccounts(JdbcTemplate jdbcTemplateObject, 
			int contactId, PaymentAccount[] paymentAccounts) {
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "insert into PAYMENT_ACCOUNTS (contactId) values (?)";
		PreparedStatementCreatorImpl psc = new PreparedStatementCreatorImpl(
				sql, new Object[] {contactId}, new String[] {"id"});

		BankAccount bankAccount;
		int paymentAccountId;
		for(PaymentAccount paymentAccount: paymentAccounts) {
			
			jdbcTemplateObject.update(psc, keyHolder);
			paymentAccountId = keyHolder.getKey().intValue();
			paymentAccount.setId(paymentAccountId);
			
			bankAccount = paymentAccount.getBankAccount();			
			if(bankAccount != null) {
				bankAccount.insertBankAccount(jdbcTemplateObject, paymentAccountId, bankAccount);
			}
		}

		
	}	
}
