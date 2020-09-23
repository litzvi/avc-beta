/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Check;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CONTACT_DETAILS", uniqueConstraints = 
	{ @UniqueConstraint(columnNames = { "companyId", "personId" }) })
@Check(constraints = "(company_id is null) XOR (person_id is null)")
public class ContactDetails extends LinkEntity {

	@ToString.Exclude
	@JsonBackReference(value = "company_contactDetails")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId", updatable = false)
	private Company company;

	@ToString.Exclude
	@JsonBackReference(value = "person_contactDetails")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId", updatable = false)
	private Person person;

	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Phone> phones = new HashSet<>();

	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Fax> faxes = new HashSet<>();

	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Email> emails = new HashSet<>();

	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonFormat(with = Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private Set<Address> addresses = new HashSet<>();

	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, 
			/* orphanRemoval = true, */ fetch = FetchType.LAZY)
	private Set<PaymentAccount> paymentAccounts = new HashSet<>();
	
	public void setPhones(Phone[] phones) {
		Ordinal.setOrdinals(phones);
		this.phones = Insertable.setReferences(phones, (t) -> {t.setReference(this);	return t;});
	}
	
	public Phone[] getPhones() {
		return (Phone[])this.phones.toArray(new Phone[this.phones.size()]);
	}
	
	public void setFaxes(Fax[] faxes) {
		Ordinal.setOrdinals(faxes);
		this.faxes = Insertable.setReferences(faxes, (t) -> {t.setReference(this);	return t;});
	}
	
	public Fax[] getFaxes() {
		return (Fax[])this.faxes.toArray(new Fax[this.faxes.size()]);
	}
	
	public void setEmails(Email[] emails) {
		Ordinal.setOrdinals(emails);
		this.emails = Insertable.setReferences(emails, (t) -> {t.setReference(this);	return t;});
	}
	
	public Email[] getEmails() {
		return (Email[])this.emails.toArray(new Email[this.emails.size()]);
	}
	
	public void setAddresses(Address[] addresses) {
		Ordinal.setOrdinals(addresses);
		this.addresses = Insertable.setReferences(addresses, (t) -> {t.setReference(this);	return t;});
	}
	
	public Address[] getAddresses() {
		return (Address[])this.addresses.toArray(new Address[this.addresses.size()]);
	}
	
	public void setPaymentAccounts(PaymentAccount[] paymentAccounts) {
		Ordinal.setOrdinals(paymentAccounts);
		this.paymentAccounts = Insertable.setReferences(paymentAccounts, 
				(t) -> {t.setReference(this);	return t;});
	}
	
	public PaymentAccount[] getPaymentAccounts() {
		return (PaymentAccount[])this.paymentAccounts.toArray(new PaymentAccount[this.paymentAccounts.size()]);
	}

//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}

	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof Company) {
			this.setCompany((Company)referenced);
		}
		else if(referenced instanceof Person) {
			this.setPerson((Person)referenced);
		}
		else {
			throw new ClassCastException("Referenced object dosen't match ContactDetails references");
		}		
	}
	
}
