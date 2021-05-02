/**
 * 
 */
package com.avc.mis.beta.service.reports;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.view.ContainerArrivalRow;
import com.avc.mis.beta.repositories.ContainerArrivalRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ContainerArrivalReports {

	@Autowired private ContainerArrivalRepository containerArrivalRepository;

	
	public List<ContainerArrivalRow> getContainerArrivals() {
		return getContainerArrivalRepository().findContainerArrivals();
	}
	
	public Set<ContainerArrivalBasic> getNonLoadedArrivals() {
		return getContainerArrivalRepository().getNonLoadedArrivals();		
	}

	
}
