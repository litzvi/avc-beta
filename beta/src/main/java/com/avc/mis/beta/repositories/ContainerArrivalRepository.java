/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.processInfo.ContainerArrivalInfo;
import com.avc.mis.beta.dto.view.ContainerArrivalRow;
import com.avc.mis.beta.entities.process.ContainerArrival;

/**
 * @author zvi
 *
 */
public interface ContainerArrivalRepository extends ProcessRepository<ContainerArrival> {

	@Query("select new com.avc.mis.beta.dto.processInfo.ContainerArrivalInfo("
			+ "p.containerDetails, p.shipingDetails, "
			+ "pc.id, pc.version, pc.name) "
		+ "from ContainerArrival p "
			+ "left join p.productCompany pc "
		+ "where p.id = :processId ")
	ContainerArrivalInfo findContainerArrivalInfo(int processId);

	@Query("select new com.avc.mis.beta.dto.view.ContainerArrivalRow( "
			+ "p.id, "
			+ "p.recordedTime, p.downtime, lc.processStatus, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(u.username, ': ', approval.decision))), "
			+ "ship.eta, cont.containerNumber, cont.sealNumber, cont.containerType, "
			+ "pc.id, pc.version, pc.name, "
			+ "shipment_code.id, shipment_code.code, pod.code, pod.value) "
		+ "from ContainerArrival p "
			+ "join p.containerDetails cont "
			+ "join p.shipingDetails ship "
			+ "left join p.productCompany pc "
			+ "join p.processType pt "
			+ "join p.lifeCycle lc "
			+ "left join p.approvals approval "
				+ "left join approval.user u "
			+ "left join p.containerLoadings loading "
				+ "left join loading.shipmentCode shipment_code "
					+ "left join shipment_code.portOfDischarge pod "
		+ "where "
			+ "(:startTime is null or p.recordedTime >= :startTime) "
			+ "and (:endTime is null or p.recordedTime < :endTime) "
		+ "group by p "
		+ "order by p.recordedTime desc ")
	List<ContainerArrivalRow> findContainerArrivals(LocalDateTime startTime, LocalDateTime endTime);

	@Query("select new com.avc.mis.beta.dto.basic.ContainerArrivalBasic("
			+ "p.id, p.version, cd.containerNumber, "
			+ "pc.id, pc.version, pc.name) "
		+ "from ContainerArrival p "
			+ "join p.containerDetails cd "
			+ "left join p.productCompany pc "
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
