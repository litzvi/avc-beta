/**
 * 
 */
package com.avc.mis.beta.dto.doc;

import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class ExportInfo {

	private ShipmentCodeBasic shipmentCode;
	private OffsetDateTime processDate;
	//vehicle
	//driver(name, phone, id)
	
	
	public ExportInfo(Integer shipmentCodeId, String shipmentCodeCode, String portOfDischargeCode, String portOfDischargeValue, 
			OffsetDateTime processDate
			) {
		super();
		this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, shipmentCodeCode, portOfDischargeCode, portOfDischargeValue);
		this.processDate = processDate;
	}
	
	
}
