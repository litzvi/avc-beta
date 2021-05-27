package com.avc.mis.beta.controllers;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.ValueTablesReader;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/qc")
public class QcController {

	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private QualityChecks qualityChecks;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@PostMapping("/addCashewReceiveCheck")
	public QualityCheckDTO addCashewReceiptCheck(@RequestBody QualityCheck check) {
		qualityChecks.addCashewReceiptCheck(check);
		return qualityChecks.getQcByProcessId(check.getId());
	}
	
	@PostMapping("/addCashewRoastCheck")
	public QualityCheckDTO addCashewRoastCheck(@RequestBody QualityCheck check) {
		check.setCheckedBy("avc lab");
		qualityChecks.addRoastedCashewCheck(check);
		return qualityChecks.getQcByProcessId(check.getId());
	}
	
	@PutMapping("/editCashewReceiveCheck")
	public QualityCheckDTO editCashewReceiveCheck(@RequestBody QualityCheck check) {
		qualityChecks.editCheck(check);
		return qualityChecks.getQcByProcessId(check.getId());
	}
	
	@RequestMapping("/getQcCheck/{id}")
	public QualityCheckDTO getQcCheck(@PathVariable("id") int processId) {
		return qualityChecks.getQcByProcessId(processId);
	}
	
	@RequestMapping("/getRawQC")
	public List<CashewQcRow> getRawQC() {
		return qualityChecks.getRawQualityChecks();
	}
	
	@RequestMapping("/getRoastQC")
	public List<CashewQcRow> getRoastQC() {
		return qualityChecks.getRoastedQualityChecks();
	}
	
	@RequestMapping("/getCashewItems")
	public List<ItemWithUnitDTO> getCashewItems() {
		return refeDao.getItemsByGroup(ItemGroup.PRODUCT);
	}
	
	@RequestMapping("/getPoCashew/{id}")
	public Set<PoCodeBasic> getPoCashew(@PathVariable("id") Boolean roast) {
		if(roast) {
			return objectTableReader.findAvailableInventoryPoCodes(new ProductionUse[]{ProductionUse.CLEAN, ProductionUse.ROAST});
		} else {
			return objectTableReader.findOpenAndPendingCashewOrdersPoCodes();
		}
	}
	
}
