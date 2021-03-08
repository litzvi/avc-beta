/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
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
public class ContainerBooking extends GeneralProcess {
	
//	@Valid
//	@NotNull(message = "Shipment code is mandatory")
//	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@JoinColumn(nullable = false, name = "shipment_code_code")
//	private ShipmentCode shipmentCode;
	
	@NotNull(message = "Shipment code is mandatory")
	@Column(nullable = false, unique = true)
	private String bookingNumber;
	
	@Column(nullable = false)
	@NotNull(message = "Booking date date is mandatory")
	@Convert(converter = LocalDateToLong.class)
	private LocalDate bookingDate;
	
	@Valid
	@Embedded
	@NotNull(message = "Shipping details is mandatory")
	private ShipingDetails shipingDetails;
		
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "logisticsCompanyId")
//	private Supplier logisticsCompany;
	
	public void setBookingDate(String bookingDate) {
		if(bookingDate != null)
			this.bookingDate = LocalDate.parse(bookingDate);
	}
		
	private String personInCharge;
	
	@OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
	private ContainerArrival containerArrival;
	
	@OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
	private ContainerLoading containerLoading;
	
}
