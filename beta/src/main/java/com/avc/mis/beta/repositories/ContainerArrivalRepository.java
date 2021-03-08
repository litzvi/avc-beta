/**
 * 
 */
package com.avc.mis.beta.repositories;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.embedable.ContainerArrivalInfo;
import com.avc.mis.beta.dto.embedable.ContainerBookingInfo;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.entities.process.ContainerBooking;

/**
 * @author zvi
 *
 */
public interface ContainerArrivalRepository extends ProcessRepository<ContainerArrival> {

	@Query("select new com.avc.mis.beta.dto.embedable.ContainerArrivalInfo("
			+ "p.containerDetails) "
		+ "from ContainerArrival p "
		+ "where p.id = :processId ")
	ContainerArrivalInfo findContainerArrivalInfo(int processId);

}
