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
import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
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
public class ContainerArrivals {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;

	@Autowired private ContainerArrivalRepository containerArrivalRepository;
	
	public Set<ContainerArrivalBasic> getNonLoadedArrivals() {
		return getContainerArrivalRepository().getNonLoadedArrivals();		
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
	 * Update the given ContainerArrival with the set data - Process information, container arriving and remarks.
	 * Ignores changed non editable fields.
	 * @param arrival ContainerArrival updated with edited state
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editArrival(ContainerArrival arrival) {
		dao.editGeneralProcessEntity(arrival);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeArrival(int arrivalId) {
		getDeletableDAO().permenentlyRemoveEntity(ContainerArrival.class, arrivalId);
	}
	
}
