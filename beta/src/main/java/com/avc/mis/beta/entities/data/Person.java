/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.avc.mis.beta.entities.interfaces.Insertable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PERSONS")
public class Person implements Insertable {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@JsonManagedReference(value = "person_idCard")
	@OneToOne(mappedBy = "person", cascade = {CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.LAZY)
	private IdCard idCard;

	@JsonManagedReference(value = "person_contactDetails")
	@OneToOne(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private ContactDetails contactDetails;
	
	public void setName(String name) {
		this.name = Optional.ofNullable(name).map(s -> s.trim()).orElse(null);;
	}

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
	
	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException("Person name can't be blank");
	}

	
}