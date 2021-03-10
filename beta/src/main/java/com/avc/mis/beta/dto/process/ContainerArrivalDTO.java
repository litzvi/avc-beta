/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.LocalDate;

import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.embedable.ContainerArrivalInfo;
import com.avc.mis.beta.dto.embedable.ContainerBookingInfo;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.entities.process.ContainerBooking;

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
	
	/**
	 * Constructor from ShipmentBooking object, used for testing.
	 * @param booking the ShipmentBooking object
	 */
	public ContainerArrivalDTO(@NonNull ContainerArrival containerArrival) {
		super(containerArrival);
//		this.shipmentCode = new ShipmentCodeDTO(containerArrival.getBooking().getShipmentCode());
		this.containerDetails = containerArrival.getContainerDetails();
		this.shipingDetails = containerArrival.getShipingDetails();
	}
	
	public void setContainerArrivalInfo(ContainerArrivalInfo info) {
//		this.shipmentCode = info.getShipmentCode();
		this.containerDetails = info.getContainerDetails();
		this.shipingDetails = info.getShipingDetails();		
	}

	
	@Override
	public String getProcessTypeDescription() {
		return getProcessName().toString();	
	}


}
