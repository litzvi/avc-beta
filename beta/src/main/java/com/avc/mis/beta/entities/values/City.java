/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.Person;
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
@Table(name="CITIES", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "countryId"})})
public class City extends ValueEntity {
	
	@Column(name = "name", nullable = false)
	private String value;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "countryId", nullable = false)
	private Country country;
	

	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof Country) {
			this.setCountry((Country)referenced);
		}
		else {
			throw new ClassCastException("City needs to have a country, Country not set");
		}		
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
	
	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "City name can't be blank";
	}
}
