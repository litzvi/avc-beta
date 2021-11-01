/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.processInfo.ContainerArrivalInfo;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.process.ContainerArrival;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ContainerArrivalDTO extends GeneralProcessDTO {

//	private ShipmentCodeDTO shipmentCode;

	private ContainerDetails containerDetails;
	private ShipingDetails shipingDetails;
	private DataObjectWithName<Supplier> productCompany; 
	
	/**
	 * Constructor from ShipmentBooking object, used for testing.
	 * @param booking the ShipmentBooking object
	 */
	public ContainerArrivalDTO(@NonNull ContainerArrival containerArrival) {
		super(containerArrival);
//		this.shipmentCode = new ShipmentCodeDTO(containerArrival.getBooking().getShipmentCode());
		this.containerDetails = containerArrival.getContainerDetails();
		this.shipingDetails = containerArrival.getShipingDetails();
		if(containerArrival.getProductCompany() != null)
			this.productCompany = new DataObjectWithName<Supplier>(containerArrival.getProductCompany());
	}
	
	public void setContainerArrivalInfo(ContainerArrivalInfo info) {
//		this.shipmentCode = info.getShipmentCode();
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

	
	@Override
	public String getProcessTypeDescription() {
		return "Container Arrival";	
	}


}
