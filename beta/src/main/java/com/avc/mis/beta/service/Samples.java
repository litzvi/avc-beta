/**
 * 
 */
package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
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
public class Samples {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired SampleRepository sampleRepository;

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addSampleReceipt(SampleReceipt sample) {
		sample.setProcessType(getSampleRepository().findProcessTypeByValue(ProcessName.SAMPLE_RECEIPET));
		dao.addProcessEntity(sample);
	}
}
