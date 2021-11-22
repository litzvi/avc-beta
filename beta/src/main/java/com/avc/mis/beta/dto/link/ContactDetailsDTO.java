/**
 * 
 */
package com.avc.mis.beta.dto.link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.LinkDTO;
import com.avc.mis.beta.dto.data.AddressDTO;
import com.avc.mis.beta.dto.data.EmailDTO;
import com.avc.mis.beta.dto.data.FaxDTO;
import com.avc.mis.beta.dto.data.PaymentAccountDTO;
import com.avc.mis.beta.dto.data.PhoneDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.data.Address;
import com.avc.mis.beta.entities.data.Email;
import com.avc.mis.beta.entities.data.Fax;
import com.avc.mis.beta.entities.data.PaymentAccount;
import com.avc.mis.beta.entities.data.Phone;
import com.avc.mis.beta.entities.link.ContactDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying ContactDetails entity data.
 * 
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ContactDetailsDTO extends LinkDTO {

	private List<PhoneDTO> phones;
	private List<FaxDTO> faxes;
	private List<EmailDTO> emails;
	private AddressDTO addresses;
	private List<PaymentAccountDTO> paymentAccounts = new ArrayList<PaymentAccountDTO>();

	/**
	 * @param contactDetails
	 */
	public ContactDetailsDTO(@NonNull ContactDetails contactDetails) {
		super(contactDetails.getId());
		this.phones = contactDetails.getPhones().stream()
				.map(p->{return new PhoneDTO(p);}).sorted(Ordinal.ordinalComparator()).collect(Collectors.toList());
		this.faxes = contactDetails.getFaxes().stream()
				.map(f->{return new FaxDTO(f);}).sorted(Ordinal.ordinalComparator()).collect(Collectors.toList());
		this.emails = contactDetails.getEmails().stream()
				.map(e->{return new EmailDTO(e);}).sorted(Ordinal.ordinalComparator()).collect(Collectors.toList());	
		this.addresses = contactDetails.getAddresses().stream().sorted(Ordinal.ordinalComparator())
				.findFirst().map(e -> {return new AddressDTO(e);}).orElse(null);
		this.paymentAccounts = contactDetails.getPaymentAccounts().stream()
				.map(p->{return new PaymentAccountDTO(p);}).sorted(Ordinal.ordinalComparator()).collect(Collectors.toList());
		
		
	}
	
	
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ContactDetails.class;
	}
	
	@Override
	public ContactDetails fillEntity(Object entity) {
		ContactDetails cdEntity;
		if(entity instanceof ContactDetails) {
			cdEntity = (ContactDetails) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ContactDetails class");
		}
		super.fillEntity(cdEntity);
		if(getPhones() != null && !getPhones().isEmpty()) {
			Ordinal.setOrdinals(getPhones());
			cdEntity.setPhones(getPhones().stream().map(i -> i.fillEntity(new Phone())).collect(Collectors.toSet()));
		}
		if(getFaxes() != null && !getFaxes().isEmpty()) {
			Ordinal.setOrdinals(getFaxes());
			cdEntity.setFaxes(getFaxes().stream().map(i -> i.fillEntity(new Fax())).collect(Collectors.toSet()));
		}
		if(getEmails() != null && !getEmails().isEmpty()) {
			Ordinal.setOrdinals(getEmails());
			cdEntity.setEmails(getEmails().stream().map(i -> i.fillEntity(new Email())).collect(Collectors.toSet()));
		}
		if(getAddresses() != null) {
			List<AddressDTO> addresses = Arrays.asList(getAddresses());
			Ordinal.setOrdinals(addresses);
			cdEntity.setAddresses(addresses.stream().map(i -> i.fillEntity(new Address())).collect(Collectors.toSet()));
		}
		if(getPaymentAccounts() != null && !getPaymentAccounts().isEmpty()) {
			Ordinal.setOrdinals(getPaymentAccounts());
			cdEntity.setPaymentAccounts(getPaymentAccounts().stream().map(i -> i.fillEntity(new PaymentAccount())).collect(Collectors.toSet()));
		}
		return cdEntity;
	}


}
