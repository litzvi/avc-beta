/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@BatchSize(size = BaseEntity.BATCH_SIZE)
@Table(name="COMPANIES", indexes = {@Index(columnList = "name", unique = true)})
@Inheritance(strategy=InheritanceType.JOINED)
public class Company extends ObjectEntityWithId {
	
	@Column(unique = true, nullable = false, updatable = false)
	private String name;
	private String localName;
	private String englishName;
	private String license;
	private String taxCode;
	private String registrationLocation;
	
	@JsonManagedReference(value = "company_contactDetails")
	@OneToOne(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, 
		optional = false, fetch = FetchType.LAZY)
	private ContactDetails contactDetails;
	
	@JsonManagedReference(value = "company_companyContacts")
	@OneToMany(mappedBy = "company",cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@BatchSize(size = BaseEntity.BATCH_SIZE)
	private Set<CompanyContact> companyContacts = new HashSet<>();
	
	public void setCompanyContacts(CompanyContact[] companyContacts) {
		this.companyContacts = Insertable.filterAndSetReference(companyContacts, 
				(t) -> {t.setReference(this);	return t;});
	}
	
	public CompanyContact[] getCompanyContacts() {
		return (CompanyContact[])this.companyContacts.toArray(new CompanyContact[this.companyContacts.size()]);
	}
	
	public void setContactDetails(ContactDetails contactDetails) {		
		if(contactDetails != null) {
			contactDetails.setReference(this);
			this.contactDetails = contactDetails;			
		}
	}
	
	public ContactDetails getContactDetails() {
		if(this.contactDetails == null) {
			setContactDetails(new ContactDetails());
		}
		return this.contactDetails;
	}
	
	public void setName(String name) {
		this.name = Optional.ofNullable(name).map(s -> s.trim()).orElse(null);
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

		
	/**
	 * @return
	 */
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(name);
	}
	
	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Company name can't be blank";
	}


}
