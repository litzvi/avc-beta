/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ObjectEntityWithName;
import com.avc.mis.beta.entities.link.ContactDetails;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="COMPANIES", indexes = {@Index(columnList = "name", unique = true)})
@Inheritance(strategy=InheritanceType.JOINED)
public class Company extends ObjectEntityWithName {
	
	private String localName;
	private String englishName;
	private String license;
	private String taxCode;
	private String registrationLocation;
	
	@JsonManagedReference(value = "company_contactDetails")
	@OneToOne(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, 
//		optional = false, 
		fetch = FetchType.LAZY)
	private ContactDetails contactDetails;
	
	@JsonManagedReference(value = "company_companyContacts")
	@OneToMany(mappedBy = "company",cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<CompanyContact> companyContacts = new HashSet<>();
	
	public void setCompanyContacts(Set<CompanyContact> companyContacts) {
		this.companyContacts = Insertable.setReferences(companyContacts, 
				(t) -> {t.setReference(this);	return t;});
	}
	
	public void setContactDetails(ContactDetails contactDetails) {		
		if(contactDetails != null) {
			contactDetails.setReference(this);
			this.contactDetails = contactDetails;			
		}
	}
	
	public ContactDetails getContactDetails() {
		return this.contactDetails;
	}

}
