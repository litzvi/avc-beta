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
public class ContactDetails {

	private Integer id;
	private Integer companyId;
	private Integer personId;
	private Phone[] phones;
	private Fax[] faxes;
	private Email[] emails;
	private Address[] addresses;
	private PaymentAccount[] paymentAccounts;

	/**
	 * @param contactDetails
	 */
	public static void insertContactDetails(JdbcTemplate jdbcTemplateObject, ContactDetails contactDetails) {
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String sql;
		int contactId;
		if(contactDetails.getCompanyId() != null) {
			sql = "insert into CONTACT_DETAILS (companyId) values (?)";
			jdbcTemplateObject.update(
					new PreparedStatementCreatorImpl(sql, 
							new Object[] {contactDetails.getCompanyId()}, new String[] {"id"}), keyHolder);			
			contactId = keyHolder.getKey().intValue();
			contactDetails.setId(contactId);
		}
		else if(contactDetails.getPersonId() != null) {
			sql = "insert into CONTACT_DETAILS (personId) values (?)";
			jdbcTemplateObject.update(
					new PreparedStatementCreatorImpl(sql, 
							new Object[] {contactDetails.getPersonId()}, new String[] {"id"}), keyHolder);			
			contactId = keyHolder.getKey().intValue();
			contactDetails.setId(contactId);
		}
		else {
			throw new IllegalArgumentException("Contact Details has to be conected to a subject (person orcompany).");
		}
		
		

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
			PaymentAccount.insertPaymentAccounts(jdbcTemplateObject, contactId, paymentAccounts);
		}
		
				
	}

	/**
	 * @param jdbcTemplateObject
	 */
	public void editContactDetails(JdbcTemplate jdbcTemplateObject) {

		if(getId() == null) {
			throw new IllegalArgumentException("Contact id can't be null");
		}
		if(getCompanyId() == null && getPersonId() == null) {
			throw new IllegalArgumentException("Subject id can't be null");
		}
		if(phones != null) {
			//search for phones without an id - to be added
			//search for phones without a name - to be removed
			//update the given phones that have id's and names
		}
		if(faxes != null) {
			//update the given phones
		}
		if(emails != null) {
			//update the given phones
		}
		if(addresses != null) {
			//update the given phones
		}
		if(paymentAccounts != null) {
			//update the given phones
		}
		
		
	}
	
	
	
}
