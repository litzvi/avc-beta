/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AddressDTO extends DataDTO {	

	private String streetAddress;
	private CityDTO city;
	
	public AddressDTO(@NonNull Address address) {
		super(address.getId(), address.getVersion());
		this.streetAddress = address.getStreetAddress();
		this.city = new CityDTO(address.getCity());
	}
}
