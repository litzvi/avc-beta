/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.LinkDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.data.ContactDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ContactDetailsDTO extends LinkDTO {

	private SortedSet<PhoneDTO> phones = new TreeSet<>(Ordinal.ordinalComparator());
	private SortedSet<FaxDTO> faxes = new TreeSet<>(Ordinal.ordinalComparator());
	private SortedSet<EmailDTO> emails = new TreeSet<>(Ordinal.ordinalComparator());
	private AddressDTO addresses;
//	private SortedSet<PaymentAccountDTO> paymentAccounts = new TreeSet<PaymentAccountDTO>(Ordinal.ordinalComparator());
	private Set<PaymentAccountDTO> paymentAccounts = new TreeSet<>(Ordinal.ordinalComparator());
	
	/**
	 * @param contactDetails
	 */
	public ContactDetailsDTO(@NonNull ContactDetails contactDetails) {
		super(contactDetails.getId());
		this.phones.addAll(Arrays.stream(contactDetails.getPhones())
				.map(p->{return new PhoneDTO(p);}).collect(Collectors.toList()));
		this.faxes.addAll(Arrays.stream(contactDetails.getFaxes())
				.map(f->{return new FaxDTO(f);}).collect(Collectors.toList()));
		this.emails.addAll(Arrays.stream(contactDetails.getEmails())
				.map(e->{return new EmailDTO(e);}).collect(Collectors.toList()));		
		this.addresses = Arrays.stream(contactDetails.getAddresses()).sorted(Ordinal.ordinalComparator())
				.findFirst().map(e -> {return new AddressDTO(e);}).orElse(null);
		this.paymentAccounts.addAll(Arrays.stream(contactDetails.getPaymentAccounts())
				.map(p->{return new PaymentAccountDTO(p);}).collect(Collectors.toList()));
		
		
		/* -- for returning subject data in order -- s
		this.phones = Arrays.stream(contactDetails.getPhones()).sorted()
				.map(p->{return new PhoneDTO(p);}).collect(Collectors.toList());
		this.faxes = Arrays.stream(contactDetails.getFaxes()).sorted(SubjectDataEntity.ordinalComparator())
				.map(f->{return new FaxDTO(f);}).collect(Collectors.toList());
		this.emails = Arrays.stream(contactDetails.getEmails()).sorted(SubjectDataEntity.ordinalComparator())
				.map(e->{return new EmailDTO(e);}).collect(Collectors.toList());
		this.addresses = Arrays.stream(contactDetails.getAddresses()).sorted(SubjectDataEntity.ordinalComparator())
				.findFirst().map(e -> {return new AddressDTO(e);}).orElse(null);
		this.paymentAccounts = Arrays.stream(contactDetails.getPaymentAccounts()).sorted(SubjectDataEntity.ordinalComparator())
				.map(p->{return new PaymentAccountDTO(p);}).collect(Collectors.toList());
		*/
	}

	/**
	 * @param phones the phones to set
	 */
	public void setPhones(Collection<PhoneDTO> phones) {
		phones.clear();
		this.phones.addAll(phones);

	}

	/**
	 * @param faxes the faxes to set
	 */
	public void setFaxes(Collection<FaxDTO> faxes) {
		faxes.clear();
		this.faxes.addAll(faxes);
	}

	/**
	 * @param emails the emails to set
	 */
	public void setEmails(Collection<EmailDTO> emails) {
		emails.clear();
		this.emails.addAll(emails);
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(Collection<AddressDTO> addresses) {
		this.addresses =  addresses.stream().sorted(Ordinal.ordinalComparator())
				.findFirst().orElse(null);
	}

	/**
	 * @param paymentAccounts the paymentAccounts to set
	 */
	public void setPaymentAccounts(Collection<PaymentAccountDTO> paymentAccounts) {
		paymentAccounts.clear();
		this.paymentAccounts.addAll(paymentAccounts);
	}
	
	

}
