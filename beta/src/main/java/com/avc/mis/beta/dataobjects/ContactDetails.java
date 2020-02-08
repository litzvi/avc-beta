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
public class ContactDetails {

	private int id;
	private int companyId;
	private Phone[] phones;
	private Fax[] faxes;
	private Email[] emails;
	private Address[] addresses;
	private PaymentAccount[] paymentAccounts;

	/**
	 * @param contactDetails
	 */
	public static void insertContactDetails(JdbcTemplate jdbcTemplateObject, ContactDetails contactDetails) {
		
		//insert record for CONTACT_DETAILS table and get back the record id
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "insert into CONTACT_DETAILS (companyID) values (?)";
		jdbcTemplateObject.update(
				new PreparedStatementCreatorImpl(sql, 
						new Object[] {contactDetails.getCompanyId()}, new String[] {"id"}), keyHolder);			
		int contactId = keyHolder.getKey().intValue();
		contactDetails.setId(contactId);

		Phone[] phones = contactDetails.getPhones();
		if(phones != null) {
			Phone.insertPhones(jdbcTemplateObject, contactId, phones);
		}	
		
		Fax[] faxes = contactDetails.getFaxes();
		if(faxes != null) {
			Fax.insertFaxes(jdbcTemplateObject, contactId, faxes);
		}

		Email[] emails = contactDetails.getEmails();
		if(emails != null) {
			Email.insertEmails(jdbcTemplateObject, contactId, emails);
		}

		Address[] addresses = contactDetails.getAddresses();
		if(addresses != null) {
			Address.insertAddresses(jdbcTemplateObject, contactId, addresses);
		}

		PaymentAccount[] paymentAccounts = contactDetails.getPaymentAccounts();
		if(paymentAccounts !=null) {
			PaymentAccount.insertPaymentAccounts(jdbcTemplateObject, contactDetails.getPaymentAccounts());
		}
		
				
	}
	
	
	
}
