/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import org.hibernate.annotations.Check;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.jdbc.core.JdbcTemplate;

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
@Table(name = "CONTACT_DETAILS", uniqueConstraints = { @UniqueConstraint(columnNames = { "companyId", "personId" }) })
@Check(constraints = "(companyId is null) xor (personId is null)")
public class ContactDetails {

	@Id
	@GeneratedValue
	private Integer id;

//	@OneToOne(targetEntity = Company.class)
//	@Column(name="companyId", insertable = false, updatable = false)
//	private Integer companyId;

	
	@ToString.Exclude @EqualsAndHashCode.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId", updatable = false)
	private Company company;

//	@Column(name = "personId")
//	private transient Integer personId;

	@OneToOne
	@JoinColumn(name = "personId")
	private Person person;

//	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "contactDetails", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	private Set<Phone> phones;

	@OneToMany(mappedBy = "contactDetails")
	private Set<Fax> faxes;

	@OneToMany(mappedBy = "contactDetails")
	private Set<Email> emails;

	@OneToMany(mappedBy = "contactDetails")
	private Set<Address> addresses;

	@OneToMany(mappedBy = "contactDetails")
	private Set<PaymentAccount> paymentAccounts;
	
	
//	public void setPhones(Set<Phone> phones) {
//		for(Phone phone: phones) {
//			phone.setContactDetails(this);
//		}
//		this.phones = phones;
//	}
	
	public void setPhones(String[] phoneNumbers) {
		this.phones = new HashSet<Phone>(phoneNumbers.length);
		Phone phone;
		for(String phoneNo: phoneNumbers) {
			phone = new Phone();
			phone.setName(phoneNo);
			phone.setContactDetails(this);
			this.phones.add(phone);
		}
	}
	


	
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
