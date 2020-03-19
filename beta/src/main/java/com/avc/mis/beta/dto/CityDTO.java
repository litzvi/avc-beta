/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;

import com.avc.mis.beta.entities.data.City;
import com.avc.mis.beta.entities.data.Email;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
public class CityDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	Integer id;
	String value;
	String countryName;
	
	@lombok.experimental.Tolerate
	public CityDTO(@NonNull City city) {
		this.id = city.getId();
		this.value = city.getValue();
		this.countryName = city.getCountry().getValue();
	}
}
