/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ShipmentBookingDTO;
import com.avc.mis.beta.dto.processinfo.BookedContainerDTO;
import com.avc.mis.beta.entities.process.ShipmentBooking;

/**
 * @author zvi
 *
 */
public interface ShipmentBookingRepository extends BaseRepository<ShipmentBooking> {

	@Query("select new com.avc.mis.beta.dto.process.ShipmentBookingDTO("
			+ "booking.id, booking.version, booking.createdDate, p_user.username, "
//			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "booking.recordedTime, booking.startTime, booking.endTime, booking.duration, booking.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, booking.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision)), "
			+ "booking.personInCharge) "
		+ "from ShipmentBooking booking "
//			+ "join booking.poCode po_code "
//				+ "join po_code.contractType t "
//				+ "join po_code.supplier s "
			+ "join booking.processType pt "
			+ "left join booking.createdBy p_user "
			+ "left join booking.productionLine p_line "
			+ "join booking.lifeCycle lc "
			+ "left join booking.approvals approval "
				+ "left join approval.user u "
		+ "where booking.id = :processId ")
	Optional<ShipmentBookingDTO> findBookingById(int processId);

	@Query("select new com.avc.mis.beta.dto.processinfo.BookedContainerDTO("
			+ "c.id, c.version, c.ordinal, c.billNumber, c.vessel, c.shippingCompany, "
			+ "port.id, port.value, "
			+ "c.etd, c.containerType) "
		+ "from BookedContainer c "
			+ "join c.destinationPort port "
			+ "join c.booking booking "
		+ "where booking.id = :processId "
		+ "order by c.ordinal ")
	List<BookedContainerDTO> findBookedContainersByProcessId(int processId);

}
