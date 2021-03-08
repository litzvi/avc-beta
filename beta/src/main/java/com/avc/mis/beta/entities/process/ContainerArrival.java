/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.TruckDetails;

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
@Table(name = "CONTAINER_ARRIVALS")
@PrimaryKeyJoinColumn(name = "processId")
public class ContainerArrival extends GeneralProcess {

	@NotNull(message = "Booking is mandatory")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "bookingId")
	private ContainerBooking booking;
	
	@Valid
	@Embedded
	@NotNull(message = "Container details is mandatory")
	private ContainerDetails containerDetails;

//	@Valid
//	@Embedded
//	@NotNull(message = "Truck details is mandatory")
//	private TruckDetails truckDetails;
	

}
