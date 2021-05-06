/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.service.reports.OrderReports;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Service for accessing and manipulating purchase orders.
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class Orders {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private PORepository poRepository;
	@Autowired private OrderReports orderReports;
		

	/**
	 * Adds a new Cashew purchase order
	 * @param po Cashew purchase order with all required details
	 * @throws IllegalArgumentException if supplier or order items aren't set.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewOrder(PO po) {
		po.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_ORDER));
		if(po.getPoCode() == null) {
			throw new IllegalArgumentException("Purchase Order has to reference a po code");
		}
		if(dao.isPoCodeFree(po.getPoCode().getId(), PoCode.class)) {
			dao.addPoProcessEntity(po);						
		}
		else {
			throw new IllegalArgumentException("Po Code is already used for another order or receipt");
		}		
	}
	
	/**
	 * Adds a new General purchase order
	 * @param po General purchase order with all required details
	 * @throws IllegalArgumentException if supplier or order items aren't set.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralOrder(PO po) {		
		po.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_ORDER));
		if(po.getPoCode() == null) {
			throw new IllegalArgumentException("Purchase Order has to reference a po code");
		}
//		addGeneralPoCode((GeneralPoCode) po.getPoCode()); - now the code is set by the user
//		dao.addEntity(po.getPoCode());
//				
//		dao.addPoProcessEntity(po);	
		if(dao.isPoCodeFree(po.getPoCode().getId(), GeneralPoCode.class)) {
			dao.addPoProcessEntity(po);						
		}
		else {
			throw new IllegalArgumentException("Po Code is already used for another order or receipt");
		}	
	}
	
	/**
	 * Gets full details of purchase order with given poCode id. 
	 * @param poCodeId the id number of the poCode
	 * @return PoDTO object with purchase order details
	 * @throws IllegalArgumentException if purchase order for given poCode dosen't exist.
	 */
	public PoDTO getOrder(int poCodeId) {
		Optional<PoDTO> orders = getPoRepository().findOrderById(null, poCodeId, new ProcessStatus[] {ProcessStatus.FINAL});
		PoDTO po = orders.orElseThrow(
				()->new IllegalArgumentException("No order with given PO code id"));
		po.setOrderItems(getPoRepository().findPoOrderItemsById(po.getId()));
		
		
		return po;
	}
	
	/**
	 * Gets full details of purchase order with given GeneralProcess id. 
	 * @param processId the id of GeneralProcess requested
	 * @return PoDTO object with purchase order details
	 * @throws IllegalArgumentException if purchase order for given process id dosen't exist.
	 */
	public PoDTO getOrderByProcessId(int processId) {
		PoDTO poDTO = new PoDTO();
		poDTO.setGeneralProcessInfo(getPoRepository()
				.findGeneralProcessInfoByProcessId(processId, PO.class)
				.orElseThrow(
						()->new IllegalArgumentException("No PO with given process id")));
		poDTO.setPoProcessInfo(getPoRepository()
				.findPoProcessInfoByProcessId(processId, PO.class)
				.orElseThrow(
						()->new IllegalArgumentException("No po code for given process id")));
		poDTO.setOrderProcessInfo(getPoRepository().findPoInfo(poDTO.getId()));
		
		poDTO.setOrderItems(getPoRepository().findPoOrderItemsById(poDTO.getId()));
		
		return poDTO;
	}
	
	/**
	 * Update the given PO with the set data - Process information, PO items and remarks.
	 * Ignores changed non editable fields.
	 * @param po PO updated with edited state
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editOrder(PO po) {
		dao.editGeneralProcessEntity(po);
	}
			
	
//----------------------------Duplicate in OrderReports - Should remove------------------------------------------
	
	
	public List<PoItemRow> findOpenCashewOrderItems() {
		List<PoItemRow> poItemRows = getOrderReports().getOrdersByType(ProcessName.CASHEW_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true);
		return poItemRows;
	}
	
	public List<PoItemRow> findAllCashewOrderItems() {
		return getOrderReports().getOrdersByType(ProcessName.CASHEW_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, false);
	}
	
	public List<PoItemRow> findAllGeneralOrderItems() {
		return getOrderReports().getOrdersByType(ProcessName.GENERAL_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, false);
	}
	
	public List<PoItemRow> findAllCashewOrderItemsHistory() {
		return getOrderReports().getOrdersByType(ProcessName.CASHEW_ORDER, ProcessStatus.values(), null, null, false);
	}
	
	public List<PoItemRow> findAllGeneralOrderItemsHistory() {
		return getOrderReports().getOrdersByType(ProcessName.GENERAL_ORDER, ProcessStatus.values(), null, null, false);
	}
	
	public List<PoItemRow> getOrdersByType(ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId) {
		return getOrderReports().getOrdersByType(orderType, processStatuses, poCodeId, null, false);
	}
		
	public List<PoItemRow> findOpenGeneralOrderItems() {
		return getOrderReports().getOrdersByType(ProcessName.GENERAL_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true);
	}
	
	
//	@Transactional(rollbackFor = Throwable.class, readOnly = false)
//	private void addOrder(PO po) {
//		//using save rather than persist in case POid was assigned by user
////		dao.addEntityWithFlexibleGenerator(po.getPoCode());
////		Session session = getEntityManager().unwrap(Session.class);
////		session.save(po.getPoCode());
//		
//		
//	}

	
}
