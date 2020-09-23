/**
 * 
 */
package com.avc.mis.beta.entities.data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.ObjectEntityWithId;
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
@Table(name = "PERSONS")
public class Person extends ObjectEntityWithId {
	
	@Column(nullable = false, updatable = false)
	@NotBlank(message = "Person name is mandetory")
	private String name;
	
	@JsonManagedReference(value = "person_idCard")
	@OneToOne(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, 
			/* orphanRemoval = true, */ fetch = FetchType.LAZY)
	private IdCard idCard;

	@JsonManagedReference(value = "person_contactDetails")
	@OneToOne(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private ContactDetails contactDetails;
	
	
	public void setIdCard(IdCard idCard) {
		if(idCard != null) {
			idCard.setReference(this);
			this.idCard = idCard;			
		}
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
	
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
}
