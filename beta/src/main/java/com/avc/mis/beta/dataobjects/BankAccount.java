/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class BankAccount {
	
	private Integer id;
	private String accountNo;
	private String ownerName;
	private Integer branchId;
	
	/**
	 * @param jdbcTemplateObject
	 * @param paymentId
	 * @param bankAccount
	 */
	public void insertBankAccount(JdbcTemplate jdbcTemplateObject, int paymentId, BankAccount bankAccount) {
		if(bankAccount == null || bankAccount.getAccountNo() == null ||
				bankAccount.getOwnerName() == null || bankAccount.getBranchId() == null) {
			throw new IllegalArgumentException("Ileagel bank account details");
		}

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "insert into BANK_ACCOUNTS (accountNo, branchId, ownerName)  values (?, ?, ?)";
		jdbcTemplateObject.update(
				new PreparedStatementCreatorImpl(sql, 
						new Object[] {bankAccount.getAccountNo(), bankAccount.getBranchId(),
								bankAccount.getOwnerName()}, new String[] {"id"}), keyHolder);			
		int accountNo = keyHolder.getKey().intValue();
		bankAccount.setId(accountNo);
		
		sql = "INSERT INTO BANK_PAYEES (paymentId, accountId) VALUES (?, ?)";
		jdbcTemplateObject.update(sql, new Object[] {paymentId, accountNo});
		
	}

}
