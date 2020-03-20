/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.io.Serializable;

import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.entities.data.Address;

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
