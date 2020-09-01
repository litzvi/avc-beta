/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.BaseDTO;
import com.avc.mis.beta.entities.process.ShipmentCode;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ShipmentCodeBasic extends BaseDTO {

	String portOfDischargeCode;
	String portOfDischargeValue;
	
	public ShipmentCodeBasic(@NonNull Integer id, String portOfDischargeCode, String portOfDischargeValue) {
		super(id);
		this.portOfDischargeCode = portOfDischargeCode;
		this.portOfDischargeValue = portOfDischargeValue;
	}

	public ShipmentCodeBasic(@NonNull ShipmentCode shipmentCode) {
		super(shipmentCode.getCode());
		this.portOfDischargeCode = shipmentCode.getPortOfDischarge().getCode();
		this.portOfDischargeValue = shipmentCode.getPortOfDischarge().getValue();
	}	
	
	/**
	 * @return a string representing full Shipment code. e.g. TAN-51284
	 */
	public String getValue() {
		return String.format("%s-%d", this.portOfDischargeCode, this.getId());
	}

}
