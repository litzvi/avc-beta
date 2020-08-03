/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.ShipmentBookingDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ShipmentBooking;
import com.avc.mis.beta.repositories.ShipmentBookingRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Service for accessing and manipulating shipment bookings.
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ShipmentBookings {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;

	@Autowired private ShipmentBookingRepository shipmentBookingRepository;

	/**
	 * Adds a new shipment booking
	 * @param booking shipment booking with all required details
	 * @throws IllegalArgumentException if booking lines aren't set.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addBooking(ShipmentBooking booking) {		
		booking.setProcessType(dao.getProcessTypeByValue(ProcessName.CONTAINER_BOOKING));
		dao.addGeneralProcessEntity(booking);	
	}
	
	/**
	 * Gets full details of shipment booking with given ShipmentBooking id. 
	 * @param processId the id of GeneralProcess requested
	 * @return ShipmentBookingDTO object with shipment booking details
	 * @throws IllegalArgumentException if shipment booking for given process id dosen't exist.
	 */
	public ShipmentBookingDTO getBooking(int processId) {
		Optional<ShipmentBookingDTO> booking = getShipmentBookingRepository().findBookingById(processId);
		ShipmentBookingDTO bookingDTO = booking.orElseThrow(
				()->new IllegalArgumentException("No shipment booking with given process id"));
		bookingDTO.setBookedContainers(getShipmentBookingRepository().findBookedContainersByProcessId(processId));
		
		return bookingDTO;
	}
	
	/**
	 * Update the given ShipmentBooking with the set data - Process information, containers booked and remarks.
	 * Ignores changed non editable fields.
	 * @param booking ShipmentBooking updated with edited state
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editBooking(ShipmentBooking booking) {
		dao.editGeneralProcessEntity(booking);
	}
		
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeBooking(int bookingId) {
		getDeletableDAO().permenentlyRemoveEntity(ShipmentBooking.class, bookingId);
	}
	
}
