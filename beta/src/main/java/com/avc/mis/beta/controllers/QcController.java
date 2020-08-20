package com.avc.mis.beta.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.processinfo.RawItemQualityDTO;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.DataObjectWithName;
import com.avc.mis.beta.dto.values.ValueObject;
import com.avc.mis.beta.dto.view.RawQcRow;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.ValueTablesReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(path = "/api/qc")
public class QcController {

	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private QualityChecks qualityChecks;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@PostMapping("/addCashewReceiveCheck/{id}")
	public QualityCheckDTO addCashewReceiptCheck(@RequestBody QualityCheck check, @PathVariable("id") String type) {
		switch (type) {
		case "avc lab":
			qualityChecks.addCashewReceiptCheck(check);
			break;
		case "supllier sample":
			qualityChecks.addCashewSampleCheck(check);
			break;
		case "supllier check":
			qualityChecks.addCashewSupplierCheck(check);
			break;
		case "vina control":
			qualityChecks.addCashewVinaControlCheck(check);
			break;

		default:
			break;
		}
		return qualityChecks.getQcByProcessId(check.getId());//getQcRawCheck(check.getId());
	}
	
	@PutMapping("/editCashewReceiveCheck")
	public QualityCheckDTO editCashewReceiveCheck(@RequestBody QualityCheck check) {
		qualityChecks.editCheck(check);
		return qualityChecks.getQcByProcessId(check.getId());//getQcRawCheck(check.getId());
	}
	
	@RequestMapping("/getQcCheck/{id}")
	public QualityCheckDTO getQcCheck(@PathVariable("id") int processId) {
		return qualityChecks.getQcByProcessId(processId);//getQcRawCheck(processId);
	}
	
	@RequestMapping("/getRawQC")
	public List<RawQcRow> getRawQC() {
		return qualityChecks.getRawQualityChecks();
	}
	
	@RequestMapping("/getCashewItems")
	public List<BasicValueEntity<Item>> getCashewItems() {
		return refeDao.getCashewItemsBasic();
	}
	
	@RequestMapping("/getPoCashewCodesOpenPending")
	public Set<PoCodeDTO> getPoCashewCodesOpenPending() {
		return objectTableReader.findOpenAndPendingCashewOrdersPoCodes();
	}
	
	@RequestMapping("/getCashewSuppliers")
	public List<DataObjectWithName> getCashewSuppliers() {
		return refeDao.getCashewSuppliersBasic();
	}
	
	
}
