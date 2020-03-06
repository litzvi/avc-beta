/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.avc.mis.beta.dataobjects.Address;
import com.avc.mis.beta.dataobjects.ContactDetails;
import com.avc.mis.beta.dataobjects.Email;
import com.avc.mis.beta.dataobjects.Fax;
import com.avc.mis.beta.dataobjects.PaymentAccount;
import com.avc.mis.beta.dataobjects.Phone;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class ContactDetailsDTO implements Serializable {

	private Integer id;
	private Phone[] phones;
	private Fax[] faxes;
	private Email[] emails;
	private Address addresses;
	private PaymentAccount[] paymentAccounts;
	
	/**
	 * @param contactDetails
	 */
	public ContactDetailsDTO(ContactDetails contactDetails) {
		this.id = contactDetails.getId();
		this.phones = contactDetails.getPhones();
		this.faxes = contactDetails.getFaxes();
		this.emails = contactDetails.getEmails();
		Address[] contactAddresses = contactDetails.getAddresses();
		this.addresses = (contactAddresses.length > 0) ? contactDetails.getAddresses()[0] : null;
		this.paymentAccounts = contactDetails.getPaymentAccounts();
	}

}
