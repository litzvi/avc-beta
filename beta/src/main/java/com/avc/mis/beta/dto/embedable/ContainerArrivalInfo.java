/**
 * 
 */
package com.avc.mis.beta.dto.embedable;

import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.entities.data.Supplier;
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
	DataObjectWithName<Supplier> productCompany; 
	
	
	public ContainerArrivalInfo(
//			Integer shipmentCodeId, String shipmentCodeCode,
//			Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode,
			ContainerDetails containerDetails, ShipingDetails shipingDetails, 
			Integer productCompanyId, Integer productCompanyVersion, String productCompanyName) {
//		this.shipmentCode = new ShipmentCodeDTO(shipmentCodeId, shipmentCodeCode, portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
		this.containerDetails = containerDetails;
		this.shipingDetails = shipingDetails;
		if(productCompanyId != null)
			this.productCompany = new DataObjectWithName<Supplier>(productCompanyId, productCompanyVersion, productCompanyName);
		else
			this.productCompany = null;
	}
}
