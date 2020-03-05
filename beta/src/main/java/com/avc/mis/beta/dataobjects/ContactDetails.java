/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Check;

import com.avc.mis.beta.dao.DAO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Entity
@Table(name = "CONTACT_DETAILS", uniqueConstraints = 
	{ @UniqueConstraint(name = "Unique subject contact details", columnNames = { "companyId", "personId" }) })
@Check(constraints = "(companyId is null) xor (personId is null)")
public class ContactDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "company_contactDetails")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId", updatable = false)
	private Company company;

	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "person_contactDetails")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId", updatable = false)
	private Person person;

	@JsonManagedReference(value = "contactDetails_phones")
	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<Phone> phones = new HashSet<>();

	@JsonManagedReference(value = "contactDetails_faxes")
	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<Fax> faxes = new HashSet<>();

	@JsonManagedReference(value = "contactDetails_emails")
	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<Email> emails = new HashSet<>();

	@JsonManagedReference(value = "contactDetails_addresses")
	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonFormat(with = Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<Address> addresses = new HashSet<>();

	@JsonManagedReference(value = "contactDetails_paymentAccount")
	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, 
		orphanRemoval = true, fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<PaymentAccount> paymentAccounts = new HashSet<>();
	
	public void setPhones(Phone[] phones) {
		this.phones = Insertable.filterAndSetReference(phones, (t) -> {t.setReference(this);	return t;});
	}
	
	public Phone[] getPhones() {
		return (Phone[])this.phones.toArray(new Phone[this.phones.size()]);
	}
	
	public void setFaxes(Fax[] faxes) {
		this.faxes = Insertable.filterAndSetReference(faxes, (t) -> {t.setReference(this);	return t;});
	}
	
	public Fax[] getFaxes() {
		return (Fax[])this.faxes.toArray(new Fax[this.faxes.size()]);
	}
	
	public void setEmails(Email[] emails) {
		this.emails = Insertable.filterAndSetReference(emails, (t) -> {t.setReference(this);	return t;});
	}
	
	public Email[] getEmails() {
		return (Email[])this.emails.toArray(new Email[this.emails.size()]);
	}
	
	public void setAddresses(Address[] addresses) {
		this.addresses = Insertable.filterAndSetReference(addresses, (t) -> {t.setReference(this);	return t;});
	}
	
	public Address[] getAddresses() {
		return (Address[])this.addresses.toArray(new Address[this.addresses.size()]);
	}
	
	public void setPaymentAccounts(PaymentAccount[] paymentAccounts) {
		this.paymentAccounts = Insertable.filterAndSetReference(paymentAccounts, 
				(t) -> {t.setReference(this);	return t;});
	}
	
	public PaymentAccount[] getPaymentAccounts() {
		return (PaymentAccount[])this.paymentAccounts.toArray(new PaymentAccount[this.paymentAccounts.size()]);
	}
		
}
