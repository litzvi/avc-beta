/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class PaymentAccount {
	private int id;
	private int contactId;
	/**
	 * @param jdbcTemplateObject
	 * @param paymentAccounts
	 */
	public static void insertPaymentAccounts(JdbcTemplate jdbcTemplateObject, PaymentAccount[] paymentAccounts) {
		// TODO Auto-generated method stub
		
	}	
}
