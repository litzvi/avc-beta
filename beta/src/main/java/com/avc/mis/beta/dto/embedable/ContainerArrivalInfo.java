/**
 * 
 */
package com.avc.mis.beta.dto.embedable;

import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ContainerArrivalInfo {

//	ShipmentCodeDTO shipmentCode;
	ContainerDetails containerDetails;
	
	
	public ContainerArrivalInfo(
//			Integer shipmentCodeId, String shipmentCodeCode,
//			Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode,
			ContainerDetails containerDetails) {
//		this.shipmentCode = new ShipmentCodeDTO(shipmentCodeId, shipmentCodeCode, portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
		this.containerDetails = containerDetails;
	}
}
