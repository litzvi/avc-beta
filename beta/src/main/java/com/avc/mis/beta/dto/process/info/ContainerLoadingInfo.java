/**
 * 
 */
package com.avc.mis.beta.dto.process.info;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.codes.ShipmentCodeDTO;

import lombok.Value;

/**
 * For fetching ContainerLoadingDTO fields.
 * 
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
