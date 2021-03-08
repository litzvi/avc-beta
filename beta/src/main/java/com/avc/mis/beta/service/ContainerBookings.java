/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.dto.basic.BookingBasic;
import com.avc.mis.beta.dto.process.ContainerArrivalDTO;
import com.avc.mis.beta.dto.process.ContainerBookingDTO;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.process.ShipmentCode;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.entities.process.ContainerBooking;
import com.avc.mis.beta.repositories.ContainerArrivalRepository;
import com.avc.mis.beta.repositories.ContainerBookingRepository;

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
public class ContainerBookings {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;

	@Autowired private ContainerBookingRepository containerBookingRepository;
	@Autowired private ContainerArrivalRepository containerArrivalRepository;
	
	public Set<BookingBasic> getNonArrivedBookings() {
		return getContainerBookingRepository().getNonArrivedBookings();		
	}
	
	public Set<BookingBasic> getNonLoadedBookings() {
		return getContainerBookingRepository().getNonLoadedBookings();		
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addShipmentCode(ShipmentCode shipmentCode) {
		dao.addEntity(shipmentCode);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editShipmentCode(ShipmentCode shipmentCode) {
		dao.editEntity(shipmentCode);
	}	


	/**
	 * Adds a new container booking
	 * @param booking shipment booking with all required details
	 * @throws IllegalArgumentException if booking lines aren't set.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addBooking(ContainerBooking booking) {		
		booking.setProcessType(dao.getProcessTypeByValue(ProcessName.CONTAINER_BOOKING));
		dao.addGeneralProcessEntity(booking);	
	}
	
	/**
	 * Gets full details of shipment booking with given ShipmentBooking id. 
	 * @param processId the id of GeneralProcess requested
	 * @return ShipmentBookingDTO object with shipment booking details
	 * @throws IllegalArgumentException if shipment booking for given process id dosen't exist.
	 */
	public ContainerBookingDTO getBooking(int processId) {
		ContainerBookingDTO containerBookingDTO = new ContainerBookingDTO();
		containerBookingDTO.setGeneralProcessInfo(getContainerBookingRepository()
				.findGeneralProcessInfoByProcessId(processId, ContainerBooking.class)
				.orElseThrow(
						()->new IllegalArgumentException("No container booking with given process id")));
		containerBookingDTO.setContainerBookingInfo(getContainerBookingRepository().findContainerBookingInfo(processId));
	
		return containerBookingDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addArrival(ContainerArrival arrival) {
		arrival.setProcessType(dao.getProcessTypeByValue(ProcessName.CONTAINER_ARRIVAL));
		dao.addGeneralProcessEntity(arrival);			
	}
	
	public ContainerArrivalDTO getArrival(int processId) {
		ContainerArrivalDTO containerArrivalDTO = new ContainerArrivalDTO();
		containerArrivalDTO.setGeneralProcessInfo(getContainerArrivalRepository()
				.findGeneralProcessInfoByProcessId(processId, ContainerArrival.class)
				.orElseThrow(
						()->new IllegalArgumentException("No container arrival with given process id")));
		containerArrivalDTO.setContainerArrivalInfo(getContainerArrivalRepository().findContainerArrivalInfo(processId));
		
		return containerArrivalDTO;
	}
	
	/**
	 * Update the given ShipmentBooking with the set data - Process information, containers booked and remarks.
	 * Ignores changed non editable fields.
	 * @param booking ShipmentBooking updated with edited state
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editBooking(ContainerBooking booking) {
		dao.editGeneralProcessEntity(booking);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editArrival(ContainerArrival arrival) {
		dao.editGeneralProcessEntity(arrival);
	}

		
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeBooking(int bookingId) {
		getDeletableDAO().permenentlyRemoveEntity(ContainerBooking.class, bookingId);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeArrival(int arrivalId) {
		getDeletableDAO().permenentlyRemoveEntity(ContainerArrival.class, arrivalId);
	}
	
}
