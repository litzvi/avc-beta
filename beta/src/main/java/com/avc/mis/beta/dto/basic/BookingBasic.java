/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDataDTO;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.process.ContainerBooking;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class BookingBasic extends BasicDataDTO {

	@JsonIgnore
	@ToString.Exclude
	@EqualsAndHashCode.Include
	String bookingNumber;
		
	public BookingBasic(Integer id, Integer version, String bookingNumber) {
		super(id, version);
		this.bookingNumber = bookingNumber;
	}
	
	public BookingBasic(@NonNull ContainerBooking booking) {
		super(booking.getId(), booking.getVersion());
		this.bookingNumber = booking.getBookingNumber();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getBookingNumber();
	}
	
}
