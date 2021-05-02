/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.LocalDate;

import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.processInfo.ContainerBookingInfo;
import com.avc.mis.beta.entities.process.ContainerBooking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ContainerBookingDTO extends GeneralProcessDTO {

//	private ShipmentCodeDTO shipmentCode;
	private String bookingNumber;
	private LocalDate bookingDate;
	
	private String personInCharge;
	

	
	/**
	 * Constructor from ShipmentBooking object, used for testing.
	 * @param booking the ShipmentBooking object
	 */
	public ContainerBookingDTO(@NonNull ContainerBooking booking) {
		super(booking);
//		this.shipmentCode = new ShipmentCodeDTO(booking.getShipmentCode());
		this.bookingNumber = booking.getBookingNumber();
		this.bookingDate = booking.getBookingDate();
		this.personInCharge = booking.getPersonInCharge();

	}
	
	public void setContainerBookingInfo(ContainerBookingInfo info) {
//		this.shipmentCode = info.getShipmentCode();
		this.bookingNumber = info.getBookingNumber();
		this.bookingDate = info.getBookingDate();
		this.personInCharge = info.getPersonInCharge();		
	}

	
	@Override
	public String getProcessTypeDescription() {
		return "Container Booking";	
	}


}
