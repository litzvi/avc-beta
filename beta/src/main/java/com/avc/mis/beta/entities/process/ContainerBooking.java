/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.utilities.LocalDateToLong;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CONTAINER_BOOKINGS")
@PrimaryKeyJoinColumn(name = "processId")
@Deprecated
public class ContainerBooking extends GeneralProcess {
	
//	@Valid
//	@NotNull(message = "Shipment code is mandatory")
//	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@JoinColumn(nullable = false, name = "shipment_code_code")
//	private ShipmentCode shipmentCode;
	
	@NotNull(message = "Booking number is mandatory")
	@Column(nullable = false, unique = true)
	private String bookingNumber;
	
	@Column(nullable = false)
	@NotNull(message = "Booking date date is mandatory")
	@Convert(converter = LocalDateToLong.class)
	private LocalDate bookingDate;
		
	private String personInCharge;
	
	public void setBookingDate(String bookingDate) {
		if(bookingDate != null)
			this.bookingDate = LocalDate.parse(bookingDate);
	}
		
//	@OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
//	private Set<ContainerArrival> containerArrivals;
	
		
}
