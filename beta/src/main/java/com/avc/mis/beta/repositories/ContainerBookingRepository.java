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
public interface ContainerBookingRepository extends ProcessRepository<ContainerBooking> {

//	@Query("select new com.avc.mis.beta.dto.process.ShipmentBookingDTO("
//			+ "booking.id, booking.version, booking.createdDate, p_user.username, "
////			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
//			+ "pt.processName, p_line, "
//			+ "booking.recordedTime, booking.startTime, booking.endTime, booking.duration, booking.numOfWorkers, "
//			+ "lc.processStatus, lc.editStatus, booking.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision)), "
//			+ "booking.personInCharge) "
//		+ "from ShipmentBooking booking "
////			+ "join booking.poCode po_code "
////				+ "join po_code.contractType t "
////				+ "join po_code.supplier s "
//			+ "join booking.processType pt "
//			+ "left join booking.createdBy p_user "
//			+ "left join booking.productionLine p_line "
//			+ "join booking.lifeCycle lc "
//			+ "left join booking.approvals approval "
//				+ "left join approval.user u "
//		+ "where booking.id = :processId ")
//	Optional<ShipmentBookingDTO> findBookingById(int processId);


	@Query("select new com.avc.mis.beta.dto.embedable.ContainerBookingInfo("
//			+ "s_code.id, s_code.code, "
//			+ "port.id, port.value, port.code, "
			+ "p.bookingDate, p.shipingDetails, p.personInCharge) "
		+ "from ContainerBooking p "
//			+ "join p.shipmentCode s_code "
//				+ "join s_code.portOfDischarge port "
		+ "where p.id = :processId ")
	ContainerBookingInfo findContainerBookingInfo(int processId);

}
