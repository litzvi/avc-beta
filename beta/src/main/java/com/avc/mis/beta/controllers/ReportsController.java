package com.avc.mis.beta.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.processinfo.RawItemQualityDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Samples;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(path = "/api/reports")
public class ReportsController {
	
	@Autowired
	private Orders ordersDao;
	
	@Autowired
	private Receipts receipts;
	
	@Autowired
	private Samples samplesControl;
	
	@Autowired
	private QualityChecks qualityChecks;
	
	@Autowired
	private ProcessInfoReader processInfoReader;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@RequestMapping("/getAllProcesses/{id}")
	public Map<String, List<Object>> getAllProcesses(@PathVariable("id") int poCode) {
		List<Object> orders = new ArrayList<Object>();
		List<Object> reciving = new ArrayList<Object>();
		List<Object> samples = new ArrayList<Object>();
		List<Object> qcsTested = new ArrayList<Object>();
		List<Object> qcsProcces = new ArrayList<Object>();
		processInfoReader.getAllProcessesByPo(poCode).forEach(k -> {
			switch (k.getProcessName()) {
				case CASHEW_ORDER:
					orders.add(ordersDao.getOrderByProcessId(k.getId()));
					break;
				case CASHEW_RECEIPT:
					reciving.add(receipts.getReceiptByProcessId(k.getId()));
					break;
				case SAMPLE_RECEIPET:
					samples.addAll(samplesControl.getSampleReceiptByProcessId(k.getId()).getSampleItems());
					break;
				case CASHEW_RECEIPT_QC:
				case VINA_CONTROL_QC:
				case SAMPLE_QC:
				case SUPPLIER_QC:
					QualityCheckDTO qualityCheckDTO = qualityChecks.getQcByProcessId(k.getId());
//					Set<RawItemQualityDTO> listChecks = qualityCheckDTO.getTestedItems();
//					listChecks.forEach(m -> {
//						Object[] array = {qualityCheckDTO.getProcessName(), m, qualityChecks.getCashewStatndard(m.getItem().getId(), "avc")};
//						qcsTested.add(array);
//					});
					qcsTested.add(qualityCheckDTO);
					qcsProcces.addAll(qualityCheckDTO.getProcessItems());
					break;
			}
		});
//		ObjectMapper mapper = new ObjectMapper();

	    // create three JSON objects
		Map<String, List<Object>> finalProcesses = new HashMap<String, List<Object>>();
//		finalProcesses.put("poCode", orders);
	    finalProcesses.put("orders", orders);
	    finalProcesses.put("reciving", reciving);
	    finalProcesses.put("samples", samples);
	    finalProcesses.put("qcsTested", qcsTested);
	    finalProcesses.put("qcsProcces", qcsProcces);
		return finalProcesses;
	}
	
	@RequestMapping("/getAllPoCodes")
	public Set<PoCodeDTO> getAllPoCodes(){
		return objectTableReader.findAllPoCodes();
	}

}
