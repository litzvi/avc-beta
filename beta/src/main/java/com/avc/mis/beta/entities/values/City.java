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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ValueEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * City entity - city with it's name-value and country reference.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="CITIES", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "countryId"})})
public class City extends ValueEntity {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "countryId", nullable = false)
	@NotNull(message = "City has to belong to a country")
	private Country country;
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof Country) {
			this.setCountry((Country)referenced);
		}
		else {
			throw new ClassCastException("City needs to have a country, Country not set");
		}		
	}

}
