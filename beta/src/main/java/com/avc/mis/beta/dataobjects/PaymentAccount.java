/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ManyToAny;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="PAYMENT_ACCOUNTS")
public class PaymentAccount {
	
	@Id @GeneratedValue
	private Integer id;
	
//	@Column(name="contactId", nullable = false)
//	private int contactId;
	
	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "contactDetails_paymentAccount")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId", updatable = false)
	private ContactDetails contactDetails;
	
	@JoinTable(name = "BANK_PAYEES", 
			joinColumns = @JoinColumn(name="paymentId", referencedColumnName="id"),
			inverseJoinColumns = @JoinColumn(name = "accountId",referencedColumnName = "id"))
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private BankAccount bankAccount;
	
	
	/**
	 * @return
	 */
	@JsonIgnore
	public boolean isLegal() {
		return getBankAccount() != null && getBankAccount().isLegal();
	}

	
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
			
			bankAccount = paymentAccount.getBankAccount();			
			if(bankAccount != null && bankAccount.getAccountNo() != null &&
					bankAccount.getOwnerName() != null && bankAccount.getBranch() != null && 
					bankAccount.getBranch().getId() != null) {
				jdbcTemplateObject.update(psc, keyHolder);
				paymentAccountId = keyHolder.getKey().intValue();
				paymentAccount.setId(paymentAccountId);
				bankAccount.insertBankAccount(jdbcTemplateObject, paymentAccountId, bankAccount);
			}
		}

		
	}



	
}
