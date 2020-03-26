/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.data.Address;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Email;
import com.avc.mis.beta.entities.data.Fax;
import com.avc.mis.beta.entities.data.PaymentAccount;
import com.avc.mis.beta.entities.data.Phone;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ContactDetailsDTO extends DataDTO {

	private Set<PhoneDTO> phones;
	private Set<FaxDTO> faxes;
	private Set<EmailDTO> emails;
	private AddressDTO addresses;
	private Set<PaymentAccountDTO> paymentAccounts;
	
	/**
	 * @param contactDetails
	 */
	public ContactDetailsDTO(@NonNull ContactDetails contactDetails) {
		super(contactDetails.getId(), contactDetails.getVersion());
		this.phones = Arrays.stream(contactDetails.getPhones()).map(p->{return new PhoneDTO(p);}).collect(Collectors.toSet());
		this.faxes = Arrays.stream(contactDetails.getFaxes()).map(f->{return new FaxDTO(f);}).collect(Collectors.toSet());
		this.emails = Arrays.stream(contactDetails.getEmails()).map(e->{return new EmailDTO(e);}).collect(Collectors.toSet());
		Address[] contactAddresses = contactDetails.getAddresses();
		this.addresses = (contactAddresses.length > 0) ? new AddressDTO(contactAddresses[0]) : null;
		this.paymentAccounts = Arrays.stream(contactDetails.getPaymentAccounts()).map(p->{return new PaymentAccountDTO(p);}).collect(Collectors.toSet());
	}

	/**
	 * @param phones the phones to set
	 */
	public void setPhones(Collection<Phone> phones) {
		this.phones = phones.stream().map(p->{return new PhoneDTO(p);}).collect(Collectors.toSet());

	}

	/**
	 * @param faxes the faxes to set
	 */
	public void setFaxes(Collection<Fax> faxes) {
		this.faxes = faxes.stream().map(f->{return new FaxDTO(f);}).collect(Collectors.toSet());
;
	}

	/**
	 * @param emails the emails to set
	 */
	public void setEmails(Collection<Email> emails) {
		this.emails = emails.stream().map(e->{return new EmailDTO(e);}).collect(Collectors.toSet());
;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(Collection<Address> addresses) {
		this.addresses =  addresses.stream().findFirst().map(e -> {return new AddressDTO(e);}).orElse(null);
	}

	/**
	 * @param paymentAccounts the paymentAccounts to set
	 */
	public void setPaymentAccounts(Collection<PaymentAccount> paymentAccounts) {
		this.paymentAccounts = paymentAccounts.stream().map(p->{return new PaymentAccountDTO(p);}).collect(Collectors.toSet());
;
	}
	
	

}
