/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessDAO;
import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.process.ContainerArrivalDTO;
import com.avc.mis.beta.dto.view.ContainerArrivalRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.repositories.ContainerArrivalRepository;
import com.avc.mis.beta.service.report.ContainerArrivalReports;

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
	
	@Autowired private ProcessDAO dao;

	@Autowired private ContainerArrivalRepository containerArrivalRepository;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public Integer addArrival(ContainerArrivalDTO arrival) {
		arrival.setProcessName(ProcessName.CONTAINER_ARRIVAL);
		return dao.addGeneralProcessEntity(arrival, ContainerArrival::new);			
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
	public void editArrival(ContainerArrivalDTO arrival) {
		dao.editGeneralProcessEntity(arrival, ContainerArrival::new);
	}
	
	//----------------------------Duplicate in ContainerArrivalReports - Should remove------------------------------------------

	@Autowired private ContainerArrivalReports containerArrivalReports;

	public List<ContainerArrivalRow> getContainerArrivals() {
		return getContainerArrivalReports().getContainerArrivals();
	}
	
	public Set<ContainerArrivalBasic> getNonLoadedArrivals() {
		return getContainerArrivalReports().getNonLoadedArrivals();		
	}
		

}
