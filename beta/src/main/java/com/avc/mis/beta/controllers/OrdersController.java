/**
 * 
 */
package com.avc.mis.beta.controllers;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.PoCodeBasicWithProductCompany;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.SampleReceiptDTO;
import com.avc.mis.beta.dto.values.PoCodeDTO;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.MixPoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.SampleReceipt;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ObjectWriter;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Samples;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.report.OrderReports;
import com.avc.mis.beta.service.report.ReceiptReports;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Zvi
 *
 */
@RestController
@RequestMapping(path = "/api/orders")
public class OrdersController {
	
	@Autowired
	private Orders ordersDao;
	
	@Autowired
	private ObjectWriter objectWriter;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private Samples samples;
	
	@Autowired
	private Receipts orderRecipt;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@Autowired
	private OrderReports orderReports;

	@Autowired
	private ProcessInfoWriter processInfoWriter;
	
	@Autowired
	private ReceiptReports receiptReports;
	
	@PostMapping(value="/addCashewOrder")
	public PoDTO addCashewOrder(@RequestBody PO po) throws InterruptedException {
		ordersDao.addCashewOrder(po);
		return ordersDao.getOrderByProcessId(po.getId());
	}
	
	@PostMapping(value="/addGeneralOrder")
	public PoDTO addGeneralOrder(@RequestBody PO po) {
		ordersDao.addGeneralOrder(po);
		return ordersDao.getOrderByProcessId(po.getId());
	}
	
	@PostMapping(value="/receiveCashewOrder")
	public ReceiptDTO receiptCashewOrder(@RequestBody Receipt receipt) {
		orderRecipt.addCashewOrderReceipt(receipt);
		return orderRecipt.getReceiptByProcessId(receipt.getId());
	}
	
	@PostMapping(value="/receiveCashewNoOrder")
	public ReceiptDTO addCashewReceipt(@RequestBody Receipt receipt) {
		orderRecipt.addCashewReceipt(receipt);
		return orderRecipt.getReceiptByProcessId(receipt.getId());
	}
	
	@PostMapping(value="/receiveGeneralOrder")
	public ReceiptDTO receiveGeneralOrder(@RequestBody Receipt receipt) {
		orderRecipt.addGeneralOrderReceipt(receipt);
		return orderRecipt.getReceiptByProcessId(receipt.getId());
	}
	
	@PostMapping(value="/receiveSample")
	public SampleReceiptDTO receiveSample(@RequestBody SampleReceipt sample) {
		samples.addSampleReceipt(sample);
		return samples.getSampleReceiptByProcessId(sample.getId());
	}
	
	
	@PutMapping(value="/editOrder")
	public PoDTO editOrder(@RequestBody PO po) {
		ordersDao.editOrder(po);
		return ordersDao.getOrderByProcessId(po.getId());
	}
	
	@PutMapping(value="/editReciving")
	public ReceiptDTO editReciving(@RequestBody Receipt receipt) {
		orderRecipt.editReceipt(receipt);
		return orderRecipt.getReceiptByProcessId(receipt.getId());
	}
	
	@PutMapping(value="/editReceiveSample")
	public SampleReceiptDTO editReceiveSample(@RequestBody SampleReceipt sample) {
		samples.editSampleReceipt(sample);
		return samples.getSampleReceiptByProcessId(sample.getId());
	}
	
	@PostMapping("/receiveExtra/{id}")
	public ReceiptDTO receiveExtra(@RequestBody Map<String, Map<String, ExtraAdded[]>> listChanges, @PathVariable("id") int reciptId) {
		listChanges.forEach((k, v) -> {
					orderRecipt.addExtra(v.get("extraAdded"), Integer.parseInt(k));
		});
		return orderRecipt.getReceiptByProcessId(reciptId);
	}
	
	@RequestMapping("/orderDetails/{id}")
	public PoDTO orderDetails(@PathVariable("id") int id) {
		return ordersDao.getOrderByProcessId(id);
	}
	
	@RequestMapping("/orderDetailsPo/{id}")
	public PoDTO orderDetailsPo(@PathVariable("id") int poCode) {
			return ordersDao.getOrder(poCode);
	}
	
	@RequestMapping("/receiveDetails/{id}")
	public ReceiptDTO receiveDetails(@PathVariable("id") int id) {
		return orderRecipt.getReceiptByProcessId(id);
	}
	
	@RequestMapping("/getCashewOrdersOpen")
	public List<PoItemRow> getCashewOrdersOpen() {
		return orderReports.findOpenCashewOrderItems();
	}
	
	@RequestMapping("/getGeneralOrdersOpen")
	public List<PoItemRow> getGeneralOrdersOpen() {
		return orderReports.findOpenGeneralOrderItems();
	}
	
	@RequestMapping("/getPendingCashew")
	public List<ReceiptRow> getPendingCashew() {
		return receiptReports.findPendingCashewReceipts();
	}
	
	@RequestMapping("/getPendingGeneral")
	public List<ReceiptRow> getPendingGeneral() {
		return receiptReports.findPendingGeneralReceipts();
	}
	
	@RequestMapping("/getReceivedCashew")
	public List<ReceiptRow> getReceivedCashew(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return receiptReports.findFinalCashewReceipts(begin, end);
	}
	
	@RequestMapping("/getReceivedGeneral")
	public List<ReceiptRow> getReceivedGeneral(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return receiptReports.findFinalGeneralReceipts(begin, end);
	}

	@RequestMapping("/getHistoryCashewOrders")
	public List<PoItemRow> getHistoryCashewOrders(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return orderReports.findAllCashewOrderItemsHistory(begin != null? begin.toLocalDate() : null, end != null? end.toLocalDate() : null);
	}
	
	@RequestMapping("/findCashewReceiptsHistory")
	public List<ReceiptRow> findCashewReceiptsHistory(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return receiptReports.findCashewReceiptsHistory(begin, end);
	}
	
	@RequestMapping("/findGeneralReceiptsHistory")
	public List<ReceiptRow> findGeneralReceiptsHistory(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return receiptReports.findGeneralReceiptsHistory(begin, end);
	}
	
	@RequestMapping("/getAllCashewReciveRejected")
	public List<ReceiptRow> getAllCashewReciveRejected() {
		return orderRecipt.findCancelledCashewReceipts();
	}
	
	@RequestMapping("/getAllGeneralOrders")
	public List<PoItemRow> getAllGeneralOrders(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return orderReports.findAllGeneralOrderItemsHistory(begin != null? begin.toLocalDate() : null, end != null? end.toLocalDate() : null);
	}
	
	@RequestMapping("/getCashewSuppliers")
	public List<DataObjectWithName<Supplier>> getCashewSuppliers() {
		return refeDao.getCashewSuppliersBasic();
	}
	
	@RequestMapping("/getGeneralSuppliers")
	public List<DataObjectWithName<Supplier>> getGeneralSuppliers() {
		return refeDao.getGeneralSuppliersBasic();
	}
	
	@RequestMapping("/getCashewPoOpen")
	public Set<PoCodeBasic> getCashewPoOpen() {
		return objectTableReader.findOpenCashewOrdersPoCodes();
	}
	
	@RequestMapping("/getPoCashewCodesOpenPending")
	public Set<PoCodeBasic> getPoCashewCodesOpenPending() {
		return objectTableReader.findOpenAndPendingCashewOrdersPoCodes();
	}
	
	@RequestMapping("/getGeneralPoOpen")
	public Set<PoCodeBasic> getGeneralPoOpen() {
		return objectTableReader.findOpenGeneralOrdersPoCodes();
	}
	
	@RequestMapping("/findFreePoCodes")
	public List<PoCodeBasic> findFreePoCodes() {
		return objectTableReader.findFreePoCodes();
	}
	
	@RequestMapping("/findAllPoCodes")
	public List<PoCodeBasicWithProductCompany> findAllPoCodes() {
		return objectTableReader.findAllPoCodes();
	}
	
	@RequestMapping("/getPoCode/{id}")
	public PoCodeDTO getPoCode(@PathVariable("id") int poCode) {
		return objectWriter.getPoCode(poCode);
	}
	
	@PostMapping(value="/addPoCode")
	public ResponseEntity<?> addPoCode(@RequestBody PoCode poCode) {
		objectWriter.addPoCode(poCode);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping(value="/editPoCode")
	public ResponseEntity<?> editPoCode(@RequestBody PoCode poCode) {
		objectWriter.editPoCode(poCode);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping("/getGeneralPoCode/{id}")
	public PoCodeDTO getGeneralPoCode(@PathVariable("id") int poCode) {
		return objectWriter.getPoCode(poCode);
	}
	
	@PostMapping(value="/addGeneralPoCode")
	public ResponseEntity<?> addGeneralPoCode(@RequestBody GeneralPoCode poCode) {
		objectWriter.addPoCode(poCode);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping(value="/editGeneralPoCode")
	public ResponseEntity<?> editGeneralPoCode(@RequestBody GeneralPoCode poCode) {
		objectWriter.editPoCode(poCode);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping("/findAllGeneralPoCodes")
	public List<PoCodeBasicWithProductCompany> findAllGeneralPoCodes() {
		return objectTableReader.findAllGeneralPoCodes();
	}
	
	@RequestMapping("/findFreeGeneralPoCodes")
	public List<PoCodeBasic> findFreeGeneralPoCodes() {
		return objectTableReader.findFreeGeneralPoCodes();
	}
	
	@PostMapping(value="/addMixPoCode")
	public ResponseEntity<?> addMixPoCode(@RequestBody MixPoCode poCode) {
		objectWriter.addMixPoCode(poCode);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping(value="/editMixPoCode")
	public ResponseEntity<?> editMixPoCode(@RequestBody MixPoCode poCode) {
		objectWriter.editMixPoCode(poCode);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping("/getAllSuppliers")
	public List<DataObjectWithName<Supplier>> getAllSuppliers() {
		return refeDao.getSuppliersBasic();
	}
	
	
	@RequestMapping("/getGeneralContractTypes")
	public List<ContractType> getGeneralContractTypes() {
		return refeDao.getGeneralContractTypes();
	}
	
	@RequestMapping("/getCashewContractTypes")
	public List<ContractType> getCashewContractTypes() {
		return refeDao.getCashewContractTypes();
	}
	
	@RequestMapping("/getSuppliersGroups")
	public List<DataObjectWithName<Supplier>> getSuppliersGroups() {
		return refeDao.getSuppliersBasicByGroup(SupplyGroup.SHIPPED_PRODUCT);
	}
	
	
}
