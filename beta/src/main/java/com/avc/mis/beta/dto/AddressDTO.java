/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Comparator;

import com.avc.mis.beta.dataobjects.Address;
import com.avc.mis.beta.dataobjects.City;
import com.avc.mis.beta.dataobjects.Phone;

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
	private City city;
	
	public AddressDTO(@NonNull Address address) {
		this.id = address.getId();
		this.streetAddress = address.getStreetAddress();
		this.city = address.getCity();
	}
}
