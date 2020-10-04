/**
 * 
 */
package com.avc.mis.beta.dto.doc;

import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.values.ShipmentCodeBasic;

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
	
	
	public ExportInfo(Integer shipmentCodeId, String portOfDischargeCode, String portOfDischargeValue, 
			OffsetDateTime processDate
			) {
		super();
		this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, portOfDischargeCode, portOfDischargeValue);
		this.processDate = processDate;
	}
	
	
}
