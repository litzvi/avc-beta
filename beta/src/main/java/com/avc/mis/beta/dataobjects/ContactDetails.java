/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
	
	@PrePersist
	public void prePersistContactDetails() {
				
		preUpdateContactDetails();
		
		paymentAccounts.removeIf(account -> (!account.isLegal()));
		for(PaymentAccount account: paymentAccounts) {
			account.setContactDetails(this);
		}
	}
	
	@PreUpdate
	public void preUpdateContactDetails() {
//		phones.removeIf(phone -> (!phone.isLegal()));
//		for(Phone phone: phones) {
//			phone.setContactDetails(this);
//		}
		
		faxes.removeIf(fax -> (!fax.isLegal()));
		for(Fax fax: faxes) {
			fax.setContactDetails(this);
		}
		
		emails.removeIf(email -> (!email.isLegal()));
		for(Email email: emails) {
			email.setContactDetails(this);
		}
		
		addresses.removeIf(address -> (!address.isLegal()));
		for(Address address: addresses) {
			address.setContactDetails(this);
		}
		
	}
	
	
//	public void setPhones(Set<Phone> phones) {
//		for(Phone phone: phones) {
//			phone.setContactDetails(this);
//		}
//		this.phones = phones;
//	}
//	
//	public void setPhones(String[] phoneNumbers) {
//		this.phones = new HashSet<Phone>(phoneNumbers.length);
//		Phone phone;
//		for(String phoneNo: phoneNumbers) {
//			phone = new Phone();
//			phone.setName(phoneNo);
//			phone.setContactDetails(this);
//			this.phones.add(phone);
//		}
//	}
	
		
	/*
	 * public static void insertContactDetails(JdbcTemplate jdbcTemplateObject,
	 * ContactDetails contactDetails) {
	 * 
	 * GeneratedKeyHolder keyHolder = new GeneratedKeyHolder(); String sql; int
	 * contactId; if(contactDetails.getCompanyId() != null) { sql =
	 * "insert into CONTACT_DETAILS (companyId) values (?)";
	 * jdbcTemplateObject.update( new PreparedStatementCreatorImpl(sql, new Object[]
	 * {contactDetails.getCompanyId()}, new String[] {"id"}), keyHolder); contactId
	 * = keyHolder.getKey().intValue(); contactDetails.setId(contactId); } else
	 * if(contactDetails.getPersonId() != null) { sql =
	 * "insert into CONTACT_DETAILS (personId) values (?)";
	 * jdbcTemplateObject.update( new PreparedStatementCreatorImpl(sql, new Object[]
	 * {contactDetails.getPersonId()}, new String[] {"id"}), keyHolder); contactId =
	 * keyHolder.getKey().intValue(); contactDetails.setId(contactId); } else {
	 * throw new
	 * IllegalArgumentException("Contact Details has to be conected to a subject (person orcompany)."
	 * ); }
	 * 
	 * 
	 * 
	 * Phone[] phones = contactDetails.getPhones(); if(phones != null) {
	 * Phone.insertPhones(jdbcTemplateObject, contactId, phones); }
	 * 
	 * Fax[] faxes = contactDetails.getFaxes(); if(faxes != null) {
	 * Fax.insertFaxes(jdbcTemplateObject, contactId, faxes); }
	 * 
	 * Email[] emails = contactDetails.getEmails(); if(emails != null) {
	 * Email.insertEmails(jdbcTemplateObject, contactId, emails); }
	 * 
	 * Address[] addresses = contactDetails.getAddresses(); if(addresses != null) {
	 * Address.insertAddresses(jdbcTemplateObject, contactId, addresses); }
	 * 
	 * PaymentAccount[] paymentAccounts = contactDetails.getPaymentAccounts();
	 * if(paymentAccounts !=null) {
	 * PaymentAccount.insertPaymentAccounts(jdbcTemplateObject, contactId,
	 * paymentAccounts); }
	 * 
	 * 
	 * }
	 */

	/**
	 * @param jdbcTemplateObject
	 *//*
		 * public void editContactDetails(JdbcTemplate jdbcTemplateObject) {
		 * 
		 * if(getId() == null) { throw new
		 * IllegalArgumentException("Contact id can't be null"); } if(getCompanyId() ==
		 * null && getPersonId() == null) { throw new
		 * IllegalArgumentException("Subject id can't be null"); } if(phones != null) {
		 * //search for phones without an id - to be added //search for phones without a
		 * name - to be removed //update the given phones that have id's and names }
		 * if(faxes != null) { //update the given phones } if(emails != null) { //update
		 * the given phones } if(addresses != null) { //update the given phones }
		 * if(paymentAccounts != null) { //update the given phones }
		 * 
		 * 
		 * }
		 */

}
