/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
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
public class ShipmentCodeDTO extends ValueDTO {
	
	String code;
	ShippingPortDTO portOfDischarge;
	
	public ShipmentCodeDTO(@NonNull Integer id, String code, Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode) {
		super(id);
		this.code = code;
		this.portOfDischarge = new ShippingPortDTO(portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
	}
	
	public ShipmentCodeDTO(ShipmentCode shipmentCode) {
		super(shipmentCode.getId());
		this.code = shipmentCode.getCode();
		this.portOfDischarge = shipmentCode.getPortOfDischarge() != null ? new ShippingPortDTO(shipmentCode.getPortOfDischarge()) : null;
	}
	
	/**
	 * @return a string representing full Shipment code. e.g. TAN-51284
	 */
	public String getValue() {
		return String.format("%s-%s", portOfDischarge.getCode(), this.getCode());
	}

//	/**
//	 * Used as a synonymous for getting id
//	 * @return the code/id
//	 */
//	public Integer getCode() {
//		return getId();
//	}



}
