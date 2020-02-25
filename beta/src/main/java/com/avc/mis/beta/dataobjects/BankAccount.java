/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="BANK_ACCOUNTS", uniqueConstraints = {@UniqueConstraint(columnNames = {"accountNo", "branchId"})})
public class BankAccount {
	
	@Id @GeneratedValue
	private Integer id;
	
	@Column(nullable = false)
	private String accountNo;
	
	@Column(nullable = false)
	private String ownerName;
		
	@JoinColumn(name="branchId", nullable = false)
	@ManyToOne(optional = false)
	private BankBranch branch;
	
	/**
	 * @param jdbcTemplateObject
	 * @param paymentId
	 * @param bankAccount
	 */
	public void insertBankAccount(JdbcTemplate jdbcTemplateObject, int paymentId, BankAccount bankAccount) {
		if(bankAccount == null || 
				bankAccount.getAccountNo() == null || bankAccount.getOwnerName() == null || 
				bankAccount.getBranch() != null || bankAccount.getBranch().getId() == null) {
			throw new IllegalArgumentException("Ileagel bank account details");
		}

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "insert into BANK_ACCOUNTS (accountNo, branchId, ownerName)  values (?, ?, ?)";
		jdbcTemplateObject.update(
				new PreparedStatementCreatorImpl(sql, 
						new Object[] {bankAccount.getAccountNo(), bankAccount.getBranch().getId(),
								bankAccount.getOwnerName()}, new String[] {"id"}), keyHolder);			
		int accountNo = keyHolder.getKey().intValue();
		bankAccount.setId(accountNo);
		
		sql = "INSERT INTO BANK_PAYEES (paymentId, accountId) VALUES (?, ?)";
		jdbcTemplateObject.update(sql, new Object[] {paymentId, accountNo});
		
	}

	/**
	 * @return
	 */
	@JsonIgnore
	public boolean isLegal() {
		return StringUtils.isNotBlank(getAccountNo()) && 
				StringUtils.isNotBlank(getOwnerName()) && getBranch() != null;
	}

}
