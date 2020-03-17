/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;

import com.avc.mis.beta.entities.data.Address;
import com.avc.mis.beta.entities.data.City;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class AddressDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private String streetAddress;
	private CityDTO city;
	
	public AddressDTO(@NonNull Address address) {
		this.id = address.getId();
		this.streetAddress = address.getStreetAddress();
		this.city = new CityDTO(address.getCity());
	}
}
