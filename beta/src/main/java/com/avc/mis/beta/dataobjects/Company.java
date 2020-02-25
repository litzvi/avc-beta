/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="COMPANIES")
@Inheritance(strategy=InheritanceType.JOINED)
public class Company {

	@Id @GeneratedValue
	private Integer id;
	
	@Column(unique = true, nullable = false)
	@NonNull
	private String name;
	private String localName;
	private String englishName;
	private String license;
	private String taxCode;
	private String registrationLocation;
	
	@JsonManagedReference(value = "company_contactDetails")
	@OneToOne(mappedBy = "company", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
	private ContactDetails contactDetails;
	
	@JsonManagedReference(value = "company_companyContacts")
	@OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
	private Set<CompanyContact> companyContacts = new HashSet<>();;
	

	@ToString.Exclude
	@Column(columnDefinition = "boolean default true", nullable = false)
	private boolean isActive = true;
	
		
	@PrePersist
	public void prePersistCompany() {
		if(contactDetails != null) {
			contactDetails.setCompany(this);
		}
		for(CompanyContact contact: companyContacts) {
			contact.setCompany(this);
		}
	}
	
	
	
	/**
	 * 
	 * @param company
	 */
	/*
	 * public static void insertCompany(JdbcTemplate jdbcTemplateObject, Company
	 * company) {
	 * 
	 * if(company.getName() == null) { throw new
	 * IllegalArgumentException("Company name can't be null"); }
	 * 
	 * String sql = "INSERT INTO COMPANIES\r\n" +
	 * "  (name, localName, englishName, license, taxCode, registrationLocation) \r\n"
	 * + "VALUES \r\n" + "  (?, ?, ?, ?, ?, ?);"; GeneratedKeyHolder keyHolder = new
	 * GeneratedKeyHolder(); Object[] parameters = new Object[] {company.getName(),
	 * company.getLocalName(), company.getEnglishName(), company.getLicense(),
	 * company.getTaxCode(), company.getRegistrationLocation()};
	 * jdbcTemplateObject.update( new PreparedStatementCreatorImpl(sql, parameters,
	 * new String[] {"id"}), keyHolder); int companyId =
	 * keyHolder.getKey().intValue(); company.setId(companyId);
	 * 
	 * ContactDetails cd = company.getContactDetails(); if(cd == null) { cd = new
	 * ContactDetails(); } cd.setCompanyId(companyId);
	 * ContactDetails.insertContactDetails(jdbcTemplateObject, cd);
	 * 
	 * CompanyContact[] companyContacts = company.getCompanyContacts();
	 * if(companyContacts != null) { for(CompanyContact cc: companyContacts) {
	 * cc.setCompanyId(companyId);
	 * CompanyContact.insertCompanyContact(jdbcTemplateObject, cc); } }
	 * 
	 * }
	 */

	/**
	 * @param jdbcTemplateObject
	 */
	/*
	 * public void editCompany(JdbcTemplate jdbcTemplateObject) { if(getId() ==
	 * null) { throw new IllegalArgumentException("Company id can't be null"); }
	 * if(name != null || localName != null || englishName != null || license !=
	 * null || taxCode != null || registrationLocation != null) { // TODO update the
	 * corresponding row in companies table } if(contactDetails != null) {
	 * contactDetails.editContactDetails(jdbcTemplateObject); } if(companyContacts2
	 * != null) { for(CompanyContact cc: companyContacts2) { //search for contacts
	 * without an id - to be added //search for phones without a name - to be
	 * removed //update the given phones that have id's and names
	 * cc.editCompanyContact(jdbcTemplateObject); } } }
	 */
}
