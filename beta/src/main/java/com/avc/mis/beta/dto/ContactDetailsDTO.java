/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class ContactDetailsDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private Set<PhoneDTO> phones;
	private Set<FaxDTO> faxes;
	private Set<EmailDTO> emails;
	private AddressDTO addresses;
	private Set<PaymentAccountDTO> paymentAccounts;
	
	/**
	 * @param contactDetails
	 */
	public ContactDetailsDTO(@NonNull ContactDetails contactDetails) {
		this.id = contactDetails.getId();
		this.phones = Arrays.stream(contactDetails.getPhones()).map(p->{return new PhoneDTO(p);}).collect(Collectors.toSet());
		this.faxes = Arrays.stream(contactDetails.getFaxes()).map(f->{return new FaxDTO(f);}).collect(Collectors.toSet());
		this.emails = Arrays.stream(contactDetails.getEmails()).map(e->{return new EmailDTO(e);}).collect(Collectors.toSet());
		Address[] contactAddresses = contactDetails.getAddresses();
		this.addresses = (contactAddresses.length > 0) ? new AddressDTO(contactAddresses[0]) : null;
		this.paymentAccounts = Arrays.stream(contactDetails.getPaymentAccounts()).map(p->{return new PaymentAccountDTO(p);}).collect(Collectors.toSet());
	}

}
