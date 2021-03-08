/**
 * 
 */
package com.avc.mis.beta.dto.embedable;

import java.time.LocalDate;

import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;

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
	ShipingDetails shipingDetails;
	String personInCharge;
	
	
	public ContainerBookingInfo(
//			Integer shipmentCodeId, String shipmentCodeCode,
//			Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode,
			String bookingNumber, LocalDate bookingDate, ShipingDetails shipingDetails, String personInCharge) {
//		this.shipmentCode = new ShipmentCodeDTO(shipmentCodeId, shipmentCodeCode, portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
		this.bookingNumber = bookingNumber;
		this.bookingDate = bookingDate;
		this.shipingDetails = shipingDetails;
		this.personInCharge = personInCharge;
	}
}
