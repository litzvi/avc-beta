/**
 * 
 */
package com.avc.mis.beta.dto.exportdoc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ExportInfo {

	ShipmentCodeBasic shipmentCode;
	LocalDateTime processDate;
	//vehicle
	//driver(name, phone, id)
	
	
	public ExportInfo(Integer shipmentCodeId, String shipmentCodeCode, String portOfDischargeCode, String portOfDischargeValue, 
			LocalDateTime processDate
			) {
		super();
		this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, shipmentCodeCode, portOfDischargeCode, portOfDischargeValue);
		this.processDate = processDate;
	}
	
	
}
