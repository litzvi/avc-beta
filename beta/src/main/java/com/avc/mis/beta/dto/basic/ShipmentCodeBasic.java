/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.process.ShipmentCode;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ShipmentCodeBasic extends BasicDTO {

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
