package com.avc.mis.beta.controllers;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.exportdoc.InventoryExportDoc;
import com.avc.mis.beta.dto.exportdoc.SecurityExportDoc;
import com.avc.mis.beta.dto.process.ContainerArrivalDTO;
import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.view.ContainerArrivalRow;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.service.ContainerArrivals;
import com.avc.mis.beta.service.ContainerBookings;
import com.avc.mis.beta.service.Loading;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ObjectWriter;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.WarehouseManagement;
import com.avc.mis.beta.service.report.ContainerArrivalReports;
import com.avc.mis.beta.service.report.LoadingReports;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/container")
public class ContainerController {

	@Autowired
	private Loading loading;
	
	@Autowired
	private ObjectWriter objectWriter;
	
	@Autowired
	private WarehouseManagement warehouseManagement;
	
	@Autowired
	private ValueTablesReader refeDao;
	
	@Autowired
	private ContainerBookings containerBookings;
	
	@Autowired
	private ContainerArrivals containerArrivals;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private LoadingReports loadingReports;
	
	@Autowired
	private ContainerArrivalReports containerArrivalReports;
	
	@PostMapping("/addLoading")
	public ContainerLoadingDTO addLoading(@RequestBody ContainerLoading load) {
		loading.addLoading(load);
		return loading.getLoading(load.getId());
	}
	
	@PutMapping("/editLoading")
	public ContainerLoadingDTO editLoading(@RequestBody ContainerLoading load) {
		loading.editLoading(load);
		return loading.getLoading(load.getId());
	}
	
	@PostMapping("/addShipmentCode")
	public ResponseEntity<Object> addShipmentCode(@RequestBody ShipmentCode shipmentCode) {
		objectWriter.addShipmentCode(shipmentCode);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/editShipmentCode")
	public ResponseEntity<Object> editShipmentCode(@RequestBody ShipmentCode shipmentCode) {
		objectWriter.editShipmentCode(shipmentCode);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/addContainerArrival")
	public ContainerArrivalDTO addContainerArrival(@RequestBody ContainerArrival arrival) {
		containerArrivals.addArrival(arrival);
		return containerArrivals.getArrival(arrival.getId());
	}
	
	@PutMapping("/editContainerArrival")
	public ContainerArrivalDTO editContainerArrival(@RequestBody ContainerArrival arrival) {
		containerArrivals.editArrival(arrival);
		return containerArrivals.getArrival(arrival.getId());
	}
	
	@RequestMapping("/getContainerArrival/{id}")
	public ContainerArrivalDTO getContainerArrival(@PathVariable("id") int processId) {
		return containerArrivals.getArrival(processId);
	}
	
	@RequestMapping("/getLoading/{id}")
	public ContainerLoadingDTO getLoading(@PathVariable("id") int processId) {
		return loading.getLoading(processId);
	}
	
	@RequestMapping("/getLoadingExportDoc/{id}")
	public InventoryExportDoc getLoadingExportDoc(@PathVariable("id") int processId) {
		return loading.getInventoryExportDoc(processId);
	}
	
	@RequestMapping("/getLoadingSecurityDoc/{id}")
	public SecurityExportDoc getLoadingSecurityDoc(@PathVariable("id") int processId) {
		return loading.getSecurityExportDoc(processId);
	}
	@RequestMapping("/getAllLoadings")
	public List<LoadingRow> getAllLoadings(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return loadingReports.getLoadings(begin, end);
	}
	
	@RequestMapping("/getStorageRoastPackedPo/{id}")
	public List<ProcessItemInventory> getStorageRoastPackedPo(@PathVariable("id") int poCode) {
		return warehouseManagement.getAvailableInventory(null, new ProductionUse[]{ProductionUse.ROAST, ProductionUse.PACKED, ProductionUse.TOFFEE}, null, null, new Integer[] {poCode}, null);
	}
	
	@RequestMapping("/getAllPosRoastPacked")
	public Set<PoCodeBasic> getAllPosRoastPacked() {
		return objectTableReader.findAvailableInventoryPoCodes(new ProductionUse[]{ProductionUse.ROAST, ProductionUse.PACKED, ProductionUse.TOFFEE});
	}
	
	@RequestMapping("/findFreeShipmentCodes")
	public List<ShipmentCodeBasic> findFreeShipmentCodes() {
		return objectTableReader.findFreeShipmentCodes();
	}
	
	@RequestMapping("/findFreeArrivals")
	public Set<ContainerArrivalBasic> findFreeArrivals() {
		return containerArrivals.getNonLoadedArrivals();
	}
	
	@RequestMapping("/findShipmentCodes")
	public List<ShipmentCodeBasic> findShipmentCodes() {
		return objectTableReader.findAllShipmentCodes();
	}
	
	@RequestMapping("/findContainerArrivals")
	public List<ContainerArrivalRow> findContainerArrivals(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return containerArrivalReports.getContainerArrivals(begin, end);
	}
	
	@RequestMapping("/getSetUpContianer")
	public List<Object> getSetUpContianer() {
		List<Object> result = new ArrayList<Object>();
		
		List<ShippingPort> ShippingPortsholder = refeDao.getAllShippingPorts();
		result.add(ShippingPortsholder);
		
		List<DataObjectWithName<Supplier>> ShippingSuppliers = refeDao.getSuppliersBasicByGroup(SupplyGroup.SHIPPED_PRODUCT);
		result.add(ShippingSuppliers);
		return result;
	}
	
}
