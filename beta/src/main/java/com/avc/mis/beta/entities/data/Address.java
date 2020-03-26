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

import org.hibernate.annotations.Where;

import com.avc.mis.beta.entities.ContactEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.values.City;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Where(clause = "deleted = false")
@Table(name="ADDRESSES")
public class Address extends ContactEntity {

	@Column(nullable = false)
	private String streetAddress;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="cityId")
	private City city;
	
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = Optional.ofNullable(streetAddress).map(s -> s.trim()).orElse(null);
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getStreetAddress());
	}
	
	@Override
	public void setReference(Object referenced) {
		this.setContactDetails((ContactDetails)referenced);
		
	}
	
	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Street address can't be blank";
	}
	
}
