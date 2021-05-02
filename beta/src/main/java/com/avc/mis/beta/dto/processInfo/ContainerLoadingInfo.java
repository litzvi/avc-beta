/**
 * 
 */
package com.avc.mis.beta.dto.processInfo;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ContainerLoadingInfo {

	ContainerArrivalBasic arrival;
	ShipmentCodeDTO shipmentCode;
	
	public ContainerLoadingInfo(Integer shipmentCodeId, String shipmentCodeCode,
			Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode, 
			Integer arrivalId, Integer arrivalVersion, String containerNumber,
			Integer productCompanyId, Integer productCompanyVersion, String productCompanyName) {
		this.shipmentCode = new ShipmentCodeDTO(shipmentCodeId, shipmentCodeCode, portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
		this.arrival = new ContainerArrivalBasic(arrivalId, arrivalVersion, containerNumber, 
				productCompanyId, productCompanyVersion, productCompanyName);
	}
	
	

}
