/**
 * 
 */
package com.avc.mis.beta.dto.process;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.dto.BaseDTO;
import com.avc.mis.beta.entities.process.ShipmentCode;
import com.avc.mis.beta.entities.values.ShippingPort;

import lombok.EqualsAndHashCode;
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
	
	String portOfDischargeCode;

	public ShipmentCodeDTO(Integer id, String portOfDischargeCode) {
		super(id);
		this.portOfDischargeCode = portOfDischargeCode;
	}
	
	public ShipmentCodeDTO(ShipmentCode shipmentCode) {
		super(shipmentCode.getCode());
		this.portOfDischargeCode = shipmentCode.getPortOfDischarge() != null ? shipmentCode.getPortOfDischarge().getCode() : null;
	}
	
	/**
	 * @return a string representing full Shipment code. e.g. TAN-51284
	 */
	public String getValue() {
		return String.format("%s-%d", portOfDischargeCode, this.getId());
	}

	/**
	 * Used as a synonymous for getting id
	 * @return the code/id
	 */
	public Integer getCode() {
		return getId();
	}



}
