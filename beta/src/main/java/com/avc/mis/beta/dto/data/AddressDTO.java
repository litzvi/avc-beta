/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.Address;
import com.avc.mis.beta.entities.process.inventory.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying Address entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AddressDTO extends SubjectDataDTO {	

	private String streetAddress;
	private CityDTO city;
	
	public AddressDTO(@NonNull Address address) {
		super(address.getId(), address.getVersion(), address.getOrdinal());
		this.streetAddress = address.getStreetAddress();
		if(address.getCity() != null)
			this.city = new CityDTO(address.getCity());
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Address.class;
	}
}
