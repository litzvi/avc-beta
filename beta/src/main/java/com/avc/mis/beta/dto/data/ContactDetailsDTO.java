/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.LinkDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.process.inventory.Storage;

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

	private SortedSet<PhoneDTO> phones = new TreeSet<>(Ordinal.ordinalComparator());
	private SortedSet<FaxDTO> faxes = new TreeSet<>(Ordinal.ordinalComparator());
	private SortedSet<EmailDTO> emails = new TreeSet<>(Ordinal.ordinalComparator());
	private AddressDTO addresses;
//	private SortedSet<PaymentAccountDTO> paymentAccounts = new TreeSet<>(Ordinal.ordinalComparator());

	private List<PaymentAccountDTO> paymentAccounts = new ArrayList<>();

	/**
	 * @param contactDetails
	 */
	public ContactDetailsDTO(@NonNull ContactDetails contactDetails) {
		super(contactDetails.getId());
		this.phones = Arrays.stream(contactDetails.getPhones())
				.map(p->{return new PhoneDTO(p);}).collect(Collectors.toCollection(() -> new TreeSet<>(Ordinal.ordinalComparator())));
		this.faxes = Arrays.stream(contactDetails.getFaxes())
				.map(f->{return new FaxDTO(f);}).collect(Collectors.toCollection(() -> new TreeSet<>(Ordinal.ordinalComparator())));
		this.emails = Arrays.stream(contactDetails.getEmails())
				.map(e->{return new EmailDTO(e);}).collect(Collectors.toCollection(() -> new TreeSet<>(Ordinal.ordinalComparator())));		
		this.addresses = Arrays.stream(contactDetails.getAddresses()).sorted(Ordinal.ordinalComparator())
				.findFirst().map(e -> {return new AddressDTO(e);}).orElse(null);
//		this.paymentAccounts = Arrays.stream(contactDetails.getPaymentAccounts())
//				.map(p->{return new PaymentAccountDTO(p);}).collect(Collectors.toCollection(() -> new TreeSet<>(Ordinal.ordinalComparator())));
		
		this.paymentAccounts = Arrays.stream(contactDetails.getPaymentAccounts())
				.map(p->{return new PaymentAccountDTO(p);}).sorted(Ordinal.ordinalComparator()).collect(Collectors.toList());
		
		
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ContactDetails.class;
	}

}
