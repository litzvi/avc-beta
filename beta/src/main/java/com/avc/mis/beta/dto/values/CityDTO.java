/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.values.City;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class CityDTO extends ValueDTO {
	
	String value;
	String countryName;

	public CityDTO(Integer id, String value, String countryName) {
		super(id);
		this.value = value;
		this.countryName = countryName;
	}
	
	public CityDTO(@NonNull City city) {
		super(city.getId());
		this.value = city.getValue();
		this.countryName = city.getCountry().getValue();
	}
}
