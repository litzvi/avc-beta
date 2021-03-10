/**
 * 
 */
package com.avc.mis.beta.dto.embedable;

import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ContainerArrivalInfo {

//	ShipmentCodeDTO shipmentCode;
	ContainerDetails containerDetails;
	ShipingDetails shipingDetails;
	
	
	public ContainerArrivalInfo(
//			Integer shipmentCodeId, String shipmentCodeCode,
//			Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode,
			ContainerDetails containerDetails, ShipingDetails shipingDetails) {
//		this.shipmentCode = new ShipmentCodeDTO(shipmentCodeId, shipmentCodeCode, portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
		this.containerDetails = containerDetails;
		this.shipingDetails = shipingDetails;
	}
}
