/**
 * 
 */
package com.avc.mis.beta.dto.processInfo;

import java.time.LocalDate;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@Deprecated
public class ContainerBookingInfo {

	String bookingNumber;
	LocalDate bookingDate;
	String personInCharge;
	
	
	public ContainerBookingInfo(
			String bookingNumber, LocalDate bookingDate, String personInCharge) {
		this.bookingNumber = bookingNumber;
		this.bookingDate = bookingDate;
		this.personInCharge = personInCharge;
	}
}
