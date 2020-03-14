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
@Table(name = "PHONES")
public class Phone extends BaseEntity {

//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;

	@ToString.Exclude
	@JsonBackReference(value = "contactDetails_phones")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId", updatable = false)
	private ContactDetails contactDetails;

	@Column(name = "phone", nullable = false)
	private String value;
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);
	}
	
	/**
	 * @return
	 */	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
	
	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException("phone number can't be blank");
	}

	@Override
	public void setReference(Object referenced) {
		this.setContactDetails((ContactDetails)referenced);
		
	}

}
