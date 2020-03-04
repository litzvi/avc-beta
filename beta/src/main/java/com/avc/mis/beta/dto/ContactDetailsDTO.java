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
	private Set<Fax> faxes = new HashSet<>();
	private Set<Email> emails = new HashSet<>();
	private Set<Address> addresses = new HashSet<>();
	private Set<PaymentAccount> paymentAccounts = new HashSet<>();
	
	/**
	 * @param contactDetails
	 */
	public ContactDetailsDTO(ContactDetails contactDetails) {
		this.id = contactDetails.getId();
		this.phones = contactDetails.getPhones();
		this.faxes.addAll(contactDetails.getFaxes());
		this.emails.addAll(contactDetails.getEmails());
		this.addresses.addAll(contactDetails.getAddresses());
		this.paymentAccounts.addAll(contactDetails.getPaymentAccounts());
	}

}
