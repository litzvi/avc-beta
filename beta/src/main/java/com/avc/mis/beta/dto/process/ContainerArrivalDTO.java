/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.dto.basic.DataObjectWithName;
import com.avc.mis.beta.dto.process.info.ContainerArrivalInfo;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.process.ContainerArrival;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for Container Arrival process
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ContainerArrivalDTO extends GeneralProcessDTO {

	private ContainerDetails containerDetails;
	private ShipingDetails shipingDetails;
	private DataObjectWithName<Supplier> productCompany; 
	
	public void setContainerArrivalInfo(ContainerArrivalInfo info) {
		this.containerDetails = info.getContainerDetails();
		this.shipingDetails = info.getShipingDetails();		
		this.productCompany = info.getProductCompany();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ContainerArrival.class;
	}
	
	@Override
	public ContainerArrival fillEntity(Object entity) {
		ContainerArrival containerArrival;
		if(entity instanceof ContainerArrival) {
			containerArrival = (ContainerArrival) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ContainerArrival class");
		}
		super.fillEntity(containerArrival);
		containerArrival.setContainerDetails(getContainerDetails());
		containerArrival.setShipingDetails(getShipingDetails());
		if(getProductCompany() != null) {
			containerArrival.setProductCompany((Supplier) getProductCompany().fillEntity(new Supplier()));
		}		
		
		return containerArrival;
	}

}
