/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.Country;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for country.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CountryDTO extends ValueDTO {
	
	private String value;

	public CountryDTO(Integer id, String value) {
		super(id);
		this.value = value;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Country.class;
	}
	
	@Override
	public Country fillEntity(Object entity) {
		Country countryEntity;
		if(entity instanceof Country) {
			countryEntity = (Country) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Country class");
		}
		super.fillEntity(countryEntity);
		countryEntity.setValue(getValue());
				
		return countryEntity;
	}

}
