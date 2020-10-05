/**
 * 
 */
package com.avc.mis.beta.dto.process;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.dto.BaseDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ShippingPortDTO;
import com.avc.mis.beta.entities.process.ShipmentCode;
import com.avc.mis.beta.entities.values.ShippingPort;

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
public class ShipmentCodeDTO extends BaseDTO {
	
	ShippingPortDTO portOfDischarge;
	
	public ShipmentCodeDTO(@NonNull Integer id, Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode) {
		super(id);
		this.portOfDischarge = new ShippingPortDTO(portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
	}
	
	public ShipmentCodeDTO(ShipmentCode shipmentCode) {
		super(shipmentCode.getCode());
		this.portOfDischarge = shipmentCode.getPortOfDischarge() != null ? new ShippingPortDTO(shipmentCode.getPortOfDischarge()) : null;
	}
	
	/**
	 * @return a string representing full Shipment code. e.g. TAN-51284
	 */
	public String getValue() {
		return String.format("%s-%d", portOfDischarge.getCode(), this.getId());
	}

	/**
	 * Used as a synonymous for getting id
	 * @return the code/id
	 */
	public Integer getCode() {
		return getId();
	}



}
