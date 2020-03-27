/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ContactEntity;
import com.avc.mis.beta.entities.Insertable;
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
//@Where(clause = "deleted = false")
@Table(name = "PHONES")
public class Phone extends ContactEntity {

	@Column(name = "phone", nullable = false)
	private String value;
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
	
	@Override
	public void setReference(Object referenced) {
		this.setContactDetails((ContactDetails)referenced);
		
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "phone number can't be blank";
	}

}
