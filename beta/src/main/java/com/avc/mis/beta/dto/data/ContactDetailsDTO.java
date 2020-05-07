/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.Arrays;
import java.util.HashSet;
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

	private SortedSet<PhoneDTO> phones = new TreeSet<PhoneDTO>(Ordinal.ordinalComparator());
	private SortedSet<FaxDTO> faxes = new TreeSet<FaxDTO>(Ordinal.ordinalComparator());
	private SortedSet<EmailDTO> emails = new TreeSet<EmailDTO>(Ordinal.ordinalComparator());
	private AddressDTO addresses;
//	private SortedSet<PaymentAccountDTO> paymentAccounts = new TreeSet<PaymentAccountDTO>(Ordinal.ordinalComparator());
	private Set<PaymentAccountDTO> paymentAccounts = new HashSet<PaymentAccountDTO>();
	
	/**
	 * @param contactDetails
	 */
	public ContactDetailsDTO(@NonNull ContactDetails contactDetails) {
		super(contactDetails.getId());
		this.phones.addAll(Arrays.stream(contactDetails.getPhones())
				.map(p->{return new PhoneDTO(p);}).collect(Collectors.toSet()));
		this.faxes.addAll(Arrays.stream(contactDetails.getFaxes())
				.map(f->{return new FaxDTO(f);}).collect(Collectors.toSet()));
		this.emails.addAll(Arrays.stream(contactDetails.getEmails())
				.map(e->{return new EmailDTO(e);}).collect(Collectors.toSet()));		
		this.addresses = Arrays.stream(contactDetails.getAddresses()).sorted(Ordinal.ordinalComparator())
				.findFirst().map(e -> {return new AddressDTO(e);}).orElse(null);
		this.paymentAccounts.addAll(Arrays.stream(contactDetails.getPaymentAccounts())
				.map(p->{return new PaymentAccountDTO(p);}).collect(Collectors.toSet()));
		
		
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

//	/**
//	 * @param phones the phones to set
//	 */
//	public void setPhones(Collection<Phone> phones) {
//		this.phones = phones.stream().sorted(SubjectDataEntity.ordinalComparator())
//				.map(p->{return new PhoneDTO(p);}).collect(Collectors.toList());
//
//	}
//
//	/**
//	 * @param faxes the faxes to set
//	 */
//	public void setFaxes(Collection<Fax> faxes) {
//		this.faxes = faxes.stream().sorted(SubjectDataEntity.ordinalComparator())
//				.map(f->{return new FaxDTO(f);}).collect(Collectors.toList());
//;
//	}
//
//	/**
//	 * @param emails the emails to set
//	 */
//	public void setEmails(Collection<Email> emails) {
//		this.emails = emails.stream().sorted(SubjectDataEntity.ordinalComparator())
//				.map(e->{return new EmailDTO(e);}).collect(Collectors.toList());
//;
//	}
//
//	/**
//	 * @param addresses the addresses to set
//	 */
//	public void setAddresses(Collection<Address> addresses) {
//		this.addresses =  addresses.stream().sorted(SubjectDataEntity.ordinalComparator())
//				.findFirst().map(e -> {return new AddressDTO(e);}).orElse(null);
//	}
//
//	/**
//	 * @param paymentAccounts the paymentAccounts to set
//	 */
//	public void setPaymentAccounts(Collection<PaymentAccount> paymentAccounts) {
//		this.paymentAccounts = paymentAccounts.stream().sorted(SubjectDataEntity.ordinalComparator())
//				.map(p->{return new PaymentAccountDTO(p);}).collect(Collectors.toList());
//
//	}
//	
	

}
