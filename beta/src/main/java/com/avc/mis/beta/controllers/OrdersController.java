/**
 * 
 */
package com.avc.mis.beta.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.SampleReceiptDTO;
import com.avc.mis.beta.dto.values.DataObjectWithName;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.dto.view.PoRow;
import com.avc.mis.beta.dto.view.ReceiptItemRow;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.SampleReceipt;
import com.avc.mis.beta.entities.processinfo.ExtraAdded;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Samples;
import com.avc.mis.beta.service.ValueTablesReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


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
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private Samples samples;
	
	@Autowired
	private Receipts orderRecipt;
	
	@Autowired
	private ValueTablesReader refeDao;
	

	@Autowired
	private ProcessInfoWriter processInfoWriter;
	
	@PostMapping(value="/addCashewOrder")
	public ResponseEntity<PoDTO> addCashewOrder(@RequestBody PO po) {
		ordersDao.addCashewOrder(po);
		return ResponseEntity.ok(ordersDao.getOrderByProcessId(po.getId()));
	}
	
	@PostMapping(value="/addGeneralOrder")
	public ResponseEntity<PoDTO> addGeneralOrder(@RequestBody PO po) {
		ordersDao.addGeneralOrder(po);
		return ResponseEntity.ok(ordersDao.getOrderByProcessId(po.getId()));
	}
	
	@PostMapping(value="/receiveCashewOrder")
	public ResponseEntity<ReceiptDTO> receiptCashewOrder(@RequestBody Receipt receipt) {
		for(Storage i : receipt.getReceiptItems()[0].getStorageForms()) {
			System.out.println(i);
		}
		orderRecipt.addCashewOrderReceipt(receipt);
		return ResponseEntity.ok(orderRecipt.getReceiptByProcessId(receipt.getId()));
	}
	
	@PostMapping(value="/receiveCashewNoOrder")
	public ResponseEntity<ReceiptDTO> addCashewReceipt(@RequestBody Receipt receipt) {
		orderRecipt.addCashewReceipt(receipt);
		return ResponseEntity.ok(orderRecipt.getReceiptByProcessId(receipt.getId()));
	}
	
	@PostMapping(value="/receiveGeneralOrder")
	public ResponseEntity<ReceiptDTO> receiveGeneralOrder(@RequestBody Receipt receipt) {
		orderRecipt.addGeneralReceipt(receipt);
		return ResponseEntity.ok(orderRecipt.getReceiptByProcessId(receipt.getId()));
	}
	
	@PostMapping(value="/receiveSample")
	public ResponseEntity<SampleReceiptDTO> receiveSample(@RequestBody SampleReceipt sample) {
		samples.addSampleReceipt(sample);
		return ResponseEntity.ok(samples.getSampleReceiptByProcessId(sample.getId()));
	}
	
	
	@PutMapping(value="/editOrder")
	public ResponseEntity<PoDTO> editOrder(@RequestBody PO po) {
		ordersDao.editOrder(po);
		return ResponseEntity.ok(ordersDao.getOrderByProcessId(po.getId()));
	}
	
	@PutMapping(value="/editReciving")
	public ResponseEntity<ReceiptDTO> editReciving(@RequestBody Receipt receipt) {
		orderRecipt.editReceipt(receipt);
		return ResponseEntity.ok(orderRecipt.getReceiptByProcessId(receipt.getId()));
	}
	
	@PutMapping(value="/editReceiveSample")
	public ResponseEntity<SampleReceiptDTO> editReceiveSample(@RequestBody SampleReceipt sample) {
		samples.editSampleReceipt(sample);
		return ResponseEntity.ok(samples.getSampleReceiptByProcessId(sample.getId()));
	}
	
	@PostMapping("/receiveExtra/{id}")
	public ResponseEntity<ReceiptDTO> receiveExtra(@RequestBody Map<String, Map<String, ExtraAdded[]>> listChanges, @PathVariable("id") int reciptId) {
		listChanges.forEach((k, v) -> {
					orderRecipt.addExtra(v.get("extraAdded"), Integer.parseInt(k));
		});
		return ResponseEntity.ok(orderRecipt.getReceiptByProcessId(reciptId));
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
		return ordersDao.findOpenCashewOrderItems();
	}
	
	@RequestMapping("/getGeneralOrdersOpen")
	public List<PoRow> getGeneralOrdersOpen() {
		return ordersDao.findOpenGeneralOrders();
	}
	
	@RequestMapping("/getPendingCashew")
	public List<ReceiptRow> getPendingCashew() {
		return orderRecipt.findPendingCashewReceipts();
	}
	
//	@RequestMapping("/getPendingGeneral")
//	public List<ReceiptRow> getPendingGeneral() {
//		return orderRecipt.fin
//	}
	
	@RequestMapping("/getReceivedCashew")
	public List<ReceiptRow> getReceivedCashew() {
		return orderRecipt.findFinalCashewReceipts();
	}
	
	@RequestMapping("/getReceivedGeneral")
	public List<ReceiptItemRow> getReceivedGeneral() {
		return orderRecipt.findFinalGeneralReceipts();
	}
	
	@RequestMapping("/getAllCashewOrders")
	public List<PoRow> getAllCashewOrders() {
		return ordersDao.findAllCashewOrders();
	}
	
	@RequestMapping("/getAllGeneralOrders")
	public List<PoRow> getAllGeneralOrders() {
		return ordersDao.findAllGeneralOrders();
	}
	
	@RequestMapping("/getCashewSuppliers")
	public List<DataObjectWithName> getCashewSuppliers() {
		return refeDao.getCashewSuppliersBasic();
	}
	
	@RequestMapping("/getGeneralSuppliers")
	public List<DataObjectWithName> getGeneralSuppliers() {
		return refeDao.getGeneralSuppliersBasic();
	}
	
	@RequestMapping("/getCashewPoOpen")
	public Set<PoCodeDTO> getCashewPoOpen() {
		return objectTableReader.findOpenCashewOrdersPoCodes();
	}
	
	@RequestMapping("/getPoCashewCodesOpenPending")
	public Set<PoCodeDTO> getPoCashewCodesOpenPending() {
		return objectTableReader.findOpenAndPendingCashewOrdersPoCodes();
	}
	
	@RequestMapping("/getGeneralPoOpen")
	public Set<PoCodeDTO> getGeneralPoOpen() {
		return objectTableReader.findOpenGeneralOrdersPoCodes();
	}
	
}
