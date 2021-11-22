/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicValueDTO;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * Shows partial information of a shipment code.
 * Used for reference and display.
 * 
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ShipmentCodeBasic extends BasicValueDTO {

	String code;
	String portOfDischargeCode;
	String portOfDischargeValue;
	
	public ShipmentCodeBasic(@NonNull Integer id, String code, String portOfDischargeCode, String portOfDischargeValue) {
		super(id);
		this.code = code;
		this.portOfDischargeCode = portOfDischargeCode;
		this.portOfDischargeValue = portOfDischargeValue;
	}
	
	/**
	 * @return a string representing full Shipment code. e.g. TAN-51284
	 */
	@Override
	public String getValue() {
		return String.format("%s-%s", this.portOfDischargeCode, this.getCode());
	}

}
