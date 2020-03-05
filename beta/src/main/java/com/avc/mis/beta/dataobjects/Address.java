/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.core.instrument.util.StringUtils;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="ADDRESSES")
public class Address implements Insertable, KeyIdentifiable {

	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
//	@Column(name="contactId", nullable = false)
//	private int contactId;
	
	@ToString.Exclude
	@JsonBackReference(value = "contactDetails_addresses")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId", updatable=false)
	private ContactDetails contactDetails;
	
	@Column(nullable = false)
	private String streetAddress;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="cityId")
	private City city;
	
	protected boolean canEqual(Object o) {
		return KeyIdentifiable.canEqualCheckNullId(this, o);
	}
	
	/**
	 * @return
	 */
	@JsonIgnore
	public boolean isLegal() {
		return StringUtils.isNotBlank(getStreetAddress());
	}
	
	@Override
	public void setReference(Object referenced) {
		this.setContactDetails((ContactDetails)referenced);
		
	}
	
}
