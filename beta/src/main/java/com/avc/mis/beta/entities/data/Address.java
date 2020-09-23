/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ContactEntity;
import com.avc.mis.beta.entities.values.City;

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
//@Where(clause = "deleted = false")
@Table(name="ADDRESSES")
public class Address extends ContactEntity {

	@Column(nullable = false)
	@NotBlank(message = "Street address is mandetory")
	private String streetAddress;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="cityId")
	@NotNull(message = "Address has to containe a city") //if city has id will already be checked by persistence context
	private City city;
	
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = Optional.ofNullable(streetAddress).map(s -> s.trim()).orElse(null);
	}
	
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
	
	@Override
	public void setReference(Object referenced) {
		this.setContactDetails((ContactDetails)referenced);
		
	}
	
}
