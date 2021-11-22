/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessDAO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.service.report.OrderReports;

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
	
	@Autowired private ProcessDAO dao;
	
	@Autowired private PORepository poRepository;
	@Autowired private OrderReports orderReports;
		

	/**
	 * Adds a new Cashew purchase order
	 * @param po Cashew purchase order with all required details
	 * @throws IllegalArgumentException if supplier or order items aren't set.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addCashewOrder(PoDTO po) {
		po.setProcessName(ProcessName.CASHEW_ORDER);
		if(po.getPoCode() == null) {
			throw new IllegalArgumentException("Purchase Order has to reference a po code");
		}
		else if(dao.isPoCodeFree(po.getPoCode().getId(), ProductPoCode.class)) {
			return dao.addPoProcessEntity(po, PO::new);						
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
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addGeneralOrder(PoDTO po) {		
		po.setProcessName(ProcessName.GENERAL_ORDER);
		if(po.getPoCode() == null) {
			throw new IllegalArgumentException("Purchase Order has to reference a po code");
		}
		else if(dao.isPoCodeFree(po.getPoCode().getId(), GeneralPoCode.class)) {
			return dao.addPoProcessEntity(po, PO::new);						
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
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void editOrder(PoDTO po) {
		dao.editPoProcessEntity(po, PO::new);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void closeOrder(Integer processId, boolean closed) {
		dao.closePO(processId, closed);
	}
			
	
//----------------------------Duplicate in OrderReports - Should remove------------------------------------------
	
	@Deprecated
	public List<PoItemRow> findOpenCashewOrderItems() {
		return getOrderReports().findOpenCashewOrderItems();
	}
	
	@Deprecated
	public List<PoItemRow> findAllCashewOrderItems() {
		return getOrderReports().findAllCashewOrderItems();
	}
	
	@Deprecated
	public List<PoItemRow> findAllGeneralOrderItems() {
		return getOrderReports().findAllGeneralOrderItems();
	}
	
	@Deprecated
	public List<PoItemRow> findAllCashewOrderItemsHistory() {
		return getOrderReports().findAllCashewOrderItemsHistory();
	}
	
	@Deprecated
	public List<PoItemRow> findAllGeneralOrderItemsHistory() {
		return getOrderReports().findAllGeneralOrderItemsHistory();
	}
	
	@Deprecated
	public List<PoItemRow> getOrdersByType(ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId) {
		return getOrderReports().getOrdersByType(orderType, processStatuses, poCodeId);
	}
		
	@Deprecated
	public List<PoItemRow> findOpenGeneralOrderItems() {
		return getOrderReports().findOpenGeneralOrderItems();
	}
	
}
