/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDataValueDTO;
import com.avc.mis.beta.dto.process.ContainerArrivalDTO;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * Shows partial information of a container arrival process.
 * Used for reference and display.
 * 
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ContainerArrivalBasic extends BasicDataValueDTO {

	@JsonIgnore
	@ToString.Exclude
	String containerNumber;

	DataObjectWithName<Supplier> productCompany; 

	public ContainerArrivalBasic(Integer id, Integer version, String containerNumber,
			Integer productCompanyId, Integer productCompanyVersion, String productCompanyName) {
		super(id, version);
		this.containerNumber = containerNumber;
		if(productCompanyId != null)
			this.productCompany = new DataObjectWithName<Supplier>(productCompanyId, productCompanyVersion, productCompanyName);
		else
			this.productCompany = null;
	}
	
	public ContainerArrivalBasic(@NonNull ContainerArrival arrival) {
		super(arrival.getId(), arrival.getVersion());
		this.containerNumber = arrival.getContainerDetails().getContainerNumber();
		if(arrival.getProductCompany() != null)
			this.productCompany = new DataObjectWithName<Supplier>(arrival.getProductCompany());
		else
			this.productCompany = null;
			
	}
	
	public ContainerArrivalBasic(@NonNull ContainerArrivalDTO arrival) {
		super(arrival.getId(), arrival.getVersion());
		this.containerNumber = arrival.getContainerDetails().getContainerNumber();
		this.productCompany = arrival.getProductCompany();			
	}

	@ToString.Include(name = "value")
	@Override
	public String getValue() {
		return getContainerNumber();
	}
	
	@Override
	public ContainerArrival fillEntity(Object entity) {
		ContainerArrival arrival;
		if(entity instanceof ContainerArrival) {
			arrival = (ContainerArrival) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ContainerArrival class");
		}
		super.fillEntity(arrival);
		ContainerDetails containerDetails = new ContainerDetails();
		containerDetails.setContainerNumber(getContainerNumber());
		arrival.setContainerDetails(containerDetails);
		if(getProductCompany() != null)
			arrival.setProductCompany((Supplier) getProductCompany().fillEntity(new Supplier()));
		
		return arrival;
	}

	
}
