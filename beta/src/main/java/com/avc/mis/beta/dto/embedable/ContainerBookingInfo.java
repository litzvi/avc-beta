/**
 * 
 */
package com.avc.mis.beta.dto.embedable;

import java.time.LocalDate;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ContainerBookingInfo {

//	ShipmentCodeDTO shipmentCode;
	String bookingNumber;
	LocalDate bookingDate;
	String personInCharge;
	
	
	public ContainerBookingInfo(
//			Integer shipmentCodeId, String shipmentCodeCode,
//			Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode,
			String bookingNumber, LocalDate bookingDate, String personInCharge) {
//		this.shipmentCode = new ShipmentCodeDTO(shipmentCodeId, shipmentCodeCode, portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
		this.bookingNumber = bookingNumber;
		this.bookingDate = bookingDate;
		this.personInCharge = personInCharge;
	}
}
