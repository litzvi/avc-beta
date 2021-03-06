/**
 * 
 */
package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.SampleReceiptDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.SampleReceipt;
import com.avc.mis.beta.repositories.SampleRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
@Deprecated
public class Samples {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired SampleRepository sampleRepository;

	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addSampleReceipt(SampleReceipt sample) {
		sample.setProcessType(dao.getProcessTypeByValue(ProcessName.SAMPLE_RECEIPET));
		dao.addPoProcessEntity(sample);
	}
	
	public SampleReceiptDTO getSampleReceiptByProcessId(int processId) {
		SampleReceiptDTO sampleReceiptDTO = new SampleReceiptDTO();
		sampleReceiptDTO.setGeneralProcessInfo(getSampleRepository()
				.findGeneralProcessInfoByProcessId(processId, SampleReceipt.class)
				.orElseThrow(
						()->new IllegalArgumentException("No receipt sample with given process id")));
		sampleReceiptDTO.setPoProcessInfo(getSampleRepository()
				.findPoProcessInfoByProcessId(processId, SampleReceipt.class)
				.orElseThrow(
						()->new IllegalArgumentException("No po code for given process id")));

		
//		Optional<SampleReceiptDTO> sample = getSampleRepository().findSampleDTOByProcessId(processId);
//		SampleReceiptDTO sampleReceiptDTO = sample.orElseThrow(
//				()->new IllegalArgumentException("No receipt sample with given process id"));
		sampleReceiptDTO.setSampleItems(getSampleRepository().findSampleItemsWithWeight(processId));
		
		return sampleReceiptDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editSampleReceipt(SampleReceipt sample) {
		dao.editGeneralProcessEntity(sample);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeSampleReceipt(int sampleId) {
		getDeletableDAO().permenentlyRemoveEntity(SampleReceipt.class, sampleId);
	}
}
