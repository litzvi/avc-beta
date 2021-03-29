/**
 * 
 */
package com.avc.mis.beta.dto.embedable;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ContainerLoadingInfo {

	ContainerArrivalBasic arrival;
	ShipmentCodeDTO shipmentCode;
	
//	ContainerDetails containerDetails;
//	ShipingDetails shipingDetails;
	
	
	public ContainerLoadingInfo(Integer shipmentCodeId, String shipmentCodeCode,
			Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode, 
			Integer arrivalId, Integer arrivalVersion, String containerNumber
//			,
//			ContainerDetails containerDetails, ShipingDetails shipingDetails
			) {
		this.shipmentCode = new ShipmentCodeDTO(shipmentCodeId, shipmentCodeCode, portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
		this.arrival = new ContainerArrivalBasic(arrivalId, arrivalVersion, containerNumber);
//		this.containerDetails = containerDetails;
//		this.shipingDetails = shipingDetails;
	}
	
	

}
