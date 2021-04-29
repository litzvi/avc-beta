/**
 * 
 */
package com.avc.mis.beta.repositories;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.embedable.ContainerBookingInfo;
import com.avc.mis.beta.entities.process.ContainerBooking;

/**
 * @author zvi
 *
 */
@Deprecated
public interface ContainerBookingRepository extends ProcessRepository<ContainerBooking> {

	@Query("select new com.avc.mis.beta.dto.embedable.ContainerBookingInfo("
			+ "p.bookingNumber, p.bookingDate, p.personInCharge) "
		+ "from ContainerBooking p "
		+ "where p.id = :processId ")
	ContainerBookingInfo findContainerBookingInfo(int processId);

}
