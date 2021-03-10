/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDataDTO;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.entities.process.ContainerBooking;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ContainerArrivalBasic extends BasicDataDTO {

	@JsonIgnore
	@ToString.Exclude
	@EqualsAndHashCode.Include
	String containerNumber;
		
	public ContainerArrivalBasic(Integer id, Integer version, String containerNumber) {
		super(id, version);
		this.containerNumber = containerNumber;
	}
	
	public ContainerArrivalBasic(@NonNull ContainerArrival arrival) {
		super(arrival.getId(), arrival.getVersion());
		this.containerNumber = arrival.getContainerDetails().getContainerNumber();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getContainerNumber();
	}
	
}
