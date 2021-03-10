/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
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
			+ "p.containerDetails, p.shipingDetails) "
		+ "from ContainerArrival p "
		+ "where p.id = :processId ")
	ContainerArrivalInfo findContainerArrivalInfo(int processId);

	@Query("select new com.avc.mis.beta.dto.basic.ContainerArrivalBasic("
			+ "p.id, p.version, cd.containerNumber) "
		+ "from ContainerArrival p "
			+ "join p.containerDetails cd "
		+ "where p.containerLoadings is empty "
			+ "or not exists ("
				+ "select p_2 "
				+ "from p.containerLoadings p_2 "
					+ "join p_2.lifeCycle lc_2 "
				+ "where "
					+ "lc_2.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
			+ ") "
		+ "order by p.recordedTime ")
	Set<ContainerArrivalBasic> getNonLoadedArrivals();

}
