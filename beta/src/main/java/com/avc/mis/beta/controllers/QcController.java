package com.avc.mis.beta.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.PackageType;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.report.QualityCheckReports;

@RestController
@RequestMapping(path = "/api/qc")
public class QcController {

	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private QualityChecks qualityChecks;
	
	@Autowired
	private QualityCheckReports qualityCheckReports;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@PostMapping("/addCashewReceiveCheck")
	public QualityCheckDTO addCashewReceiptCheck(@RequestBody QualityCheckDTO check) {
		Integer id = qualityChecks.addCashewReceiptCheck(check);
		return qualityChecks.getQcByProcessId(id);
	}
	
	@PostMapping("/addCashewRoastCheck")
	public QualityCheckDTO addCashewRoastCheck(@RequestBody QualityCheckDTO check) {
		check.setCheckedBy("avc lab");
		Integer id = qualityChecks.addRoastedCashewCheck(check);
		return qualityChecks.getQcByProcessId(id);
	}
	
	@PutMapping("/editCashewReceiveCheck")
	public QualityCheckDTO editCashewReceiveCheck(@RequestBody QualityCheckDTO check) {
		qualityChecks.editCheck(check);
		return qualityChecks.getQcByProcessId(check.getId());
	}
	
	@RequestMapping("/getQcCheck/{id}")
	public QualityCheckDTO getQcCheck(@PathVariable("id") int processId) {
		return qualityChecks.getQcByProcessId(processId);
	}
	
	@RequestMapping("/getRawQC")
	public List<CashewQcRow> getRawQC(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return qualityCheckReports.getRawQualityChecks(begin, end);
	}
	
	@RequestMapping("/getRoastQC")
	public List<CashewQcRow> getRoastQC(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return qualityCheckReports.getRoastedQualityChecks(begin, end);
	}
	
	@RequestMapping("/getCashewItems")
	public List<ItemDTO> getCashewItems() {
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
	
	@RequestMapping("/getItemsCashewBulk/{id}")
	public List<ItemDTO> getItemsCashewBulk(@PathVariable("id") Boolean roast) {
		if(roast) {
			return refeDao.getItems(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.CLEAN, ProductionUse.ROAST}, PackageType.BULK);
		} else {
			return refeDao.getItems(ItemGroup.PRODUCT, new ProductionUse[]{ProductionUse.ROAST, ProductionUse.RAW_KERNEL}, PackageType.BULK);
		}
	}
	
}
