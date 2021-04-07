/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDataDTO;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.process.ContainerArrival;
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

	@EqualsAndHashCode.Include
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
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getContainerNumber();
	}
	
}
