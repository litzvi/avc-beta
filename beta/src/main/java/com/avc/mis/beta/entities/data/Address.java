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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="ADDRESSES")
public class Address extends BaseEntity {

//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
	
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
	
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = Optional.ofNullable(streetAddress).map(s -> s.trim()).orElse(null);
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
		return StringUtils.isNotBlank(getStreetAddress());
	}
	
	@Override
	public void setReference(Object referenced) {
		this.setContactDetails((ContactDetails)referenced);
		
	}
	

	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException("Street address can't be blank");
	}
	
}
