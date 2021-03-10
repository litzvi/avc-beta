/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.embedable.ContainerBookingInfo;
import com.avc.mis.beta.entities.process.ContainerBooking;

/**
 * @author zvi
 *
 */
@Deprecated
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
			+ "p.bookingNumber, p.bookingDate, p.personInCharge) "
		+ "from ContainerBooking p "
//			+ "join p.shipmentCode s_code "
//				+ "join s_code.portOfDischarge port "
		+ "where p.id = :processId ")
	ContainerBookingInfo findContainerBookingInfo(int processId);

//	@Query("select new com.avc.mis.beta.dto.basic.BookingBasic("
//			+ "id, version, bookingNumber) "
//		+ "from ContainerBooking p "
//		+ "where p.containerArrivals is empty "
//			+ "or not exists ("
//				+ "select p_2 "
//				+ "from p.containerArrivals p_2 "
//					+ "join p_2.lifeCycle lc_2 "
//				+ "where "
//					+ "lc_2.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
//			+ ") "
//		+ "order by p.bookingDate ")
//	Set<BookingBasic> getNonArrivedBookings();

//	@Query("select new com.avc.mis.beta.dto.basic.BookingBasic("
//			+ "id, version, bookingNumber) "
//		+ "from ContainerBooking p "
//		+ "where p.containerLoadings is empty "
//			+ "or not exists ("
//				+ "select p_2 "
//				+ "from p.containerLoadings p_2 "
//					+ "join p_2.lifeCycle lc_2 "
//				+ "where "
//					+ "lc_2.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
//			+ ") "
//		+ "order by p.bookingDate ")
//	Set<BookingBasic> getNonLoadedBookings();

}
