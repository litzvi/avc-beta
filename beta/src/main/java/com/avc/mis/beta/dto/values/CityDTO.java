/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.Country;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO for city.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CityDTO extends ValueDTO {
	
	private String value;
	private String countryName;
	private BasicValueEntity<Country> country;

	public CityDTO(Integer id, String value, Integer countryId, String countryValue) {
		super(id);
		this.value = value;
		this.countryName = countryValue;
		this.country = new BasicValueEntity<Country>(countryId, countryValue);
	}
	
	public CityDTO(@NonNull City city) {
		super(city.getId());
		this.value = city.getValue();
		if(city.getCountry() != null) {
			this.countryName = city.getCountry().getValue();
			this.country = new BasicValueEntity<Country>(city.getCountry());
		}
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return City.class;
	}
	
	@Override
	public City fillEntity(Object entity) {
		City cityEntity;
		if(entity instanceof City) {
			cityEntity = (City) entity;
		}
		else {
			throw new IllegalStateException("Param has to be City class");
		}
		super.fillEntity(cityEntity);
		cityEntity.setValue(getValue());
		if(getCountry() != null) {
			cityEntity.setCountry((Country) getCountry().fillEntity(new Country()));
		}
		
		return cityEntity;
	}

}
