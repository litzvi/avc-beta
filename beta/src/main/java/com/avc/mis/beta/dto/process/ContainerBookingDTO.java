/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.LocalDate;

import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.embedable.ContainerBookingInfo;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
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
	private ShipingDetails shipingDetails;
	
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
		this.shipingDetails = booking.getShipingDetails();
		this.personInCharge = booking.getPersonInCharge();

	}
	
	public void setContainerBookingInfo(ContainerBookingInfo info) {
//		this.shipmentCode = info.getShipmentCode();
		this.bookingNumber = info.getBookingNumber();
		this.bookingDate = info.getBookingDate();
		this.shipingDetails = info.getShipingDetails();
		this.personInCharge = info.getPersonInCharge();		
	}

	
	@Override
	public String getProcessTypeDescription() {
		return getProcessName().toString();	
	}


}
