/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.dto.view.PoRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.repositories.PORepository;

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
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	@Autowired private PORepository poRepository;	
	
	/**
	 * Get all cashew orders with the order status - pending, received, rejected but not cancelled.
	 * @return list of PoRow for all orders
	 */
	@Deprecated
	public List<PoRow> findAllCashewOrders() {
		List<PoItemRow> itemRows = getPoRepository().findAllOrdersByType(ProcessName.CASHEW_ORDER,
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING});
		return getPoRows(itemRows);
	}
	
	/**
	 * Get all cashew orders with the order status - pending, received, rejected or cancelled.
	 * @return list of PoRow for all orders
	 */
	public List<PoRow> findAllCashewOrdersHistory() {
		List<PoItemRow> itemRows = getPoRepository().findAllOrdersByType(ProcessName.CASHEW_ORDER, ProcessStatus.values());
		return getPoRows(itemRows);
	}
	
	/**
	 * Gat all General orders with the order status - pending, received, rejected but not cancelled.
	 * @return list of PoRow for all orders
	 */
	public List<PoRow> findAllGeneralOrders() {
		List<PoItemRow> itemRows = getPoRepository().findAllOrdersByType(ProcessName.GENERAL_ORDER,
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING});
		return getPoRows(itemRows);
	}
	
	/**
	 * Get the table of all Cashew purchase orders that are active and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	public List<PoRow> findOpenCashewOrders() {
		List<PoItemRow> itemRows = getPoRepository().findOpenOrdersByType(ProcessName.CASHEW_ORDER);
		
		return getPoRows(itemRows);
	}
	
	/**
	 * Get the table of all General purchase orders that are active and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	public List<PoRow> findOpenGeneralOrders() {
		List<PoItemRow> itemRows = getPoRepository().findOpenOrdersByType(ProcessName.GENERAL_ORDER);
		
		return getPoRows(itemRows);
	}
	
	private List<PoRow> getPoRows(List<PoItemRow> itemRows) {
		Map<Integer, List<PoItemRow>> poMap = itemRows.stream()
				.collect(Collectors.groupingBy(PoItemRow::getId, Collectors.toList()));
		List<PoRow> poRows = new ArrayList<PoRow>();
		poMap.forEach((k, v) -> {
			PoRow poRow = new PoRow(k, v);
			poRows.add(poRow);
		});
		
		return poRows;
	}
	
	/**
	 * Get the table of all Cashew purchase orders that are active(not cancelled or archived) and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	public List<PoItemRow> findOpenCashewOrderItems() {
		List<PoItemRow> poItemRows = getPoRepository().findOpenOrdersByType(ProcessName.CASHEW_ORDER);
		return poItemRows;
	}
	
	/**
	 * Get the table of all Cashew purchase orders that are not cancelled.
	 * @return list of PoRow for all orders (not cancelled)
	 */
	public List<PoItemRow> findAllCashewOrderItems() {
		List<PoItemRow> poItemRows = getPoRepository().findAllOrdersByType(ProcessName.CASHEW_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING});
		return poItemRows;
	}
	
	/**
	 * Get the table of all Cashew purchase orders including cancelled orders.
	 * @return list of PoRow for all orders
	 */
	public List<PoItemRow> findAllCashewOrderItemsHistory() {
		List<PoItemRow> poItemRows = getPoRepository().findAllOrdersByType(ProcessName.CASHEW_ORDER, ProcessStatus.values());
		return poItemRows;
	}
	
	/**
	 * Get the table of all General purchase orders that are active(not cancelled or archived) and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	public List<PoItemRow> findOpenGeneralOrderItems() {
		return getPoRepository().findOpenOrdersByType(ProcessName.GENERAL_ORDER);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addPoCode(PoCode poCode) {
		dao.addEntity(poCode);
	}	
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addOrder(PO po) {
		//using save rather than persist in case POid was assigned by user
//		dao.addEntityWithFlexibleGenerator(po.getPoCode());
//		Session session = getEntityManager().unwrap(Session.class);
//		session.save(po.getPoCode());

		//TODO check poCode is available
		dao.addGeneralProcessEntity(po);			
	}

	/**
	 * Adds a new Cashew purchase order
	 * @param po Cashew purchase order with all required details
	 * @throws IllegalArgumentException if supplier or order items aren't set.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewOrder(PO po) {
		po.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_ORDER));
		addOrder(po);		
	}
	
	/**
	 * Adds a new General purchase order
	 * @param po General purchase order with all required details
	 * @throws IllegalArgumentException if supplier or order items aren't set.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralOrder(PO po) {		
		po.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_ORDER));
		addOrder(po);	
	}
	
	/**
	 * Gets full details of purchase order with given poCode id. 
	 * @param poCodeId the id number of the poCode
	 * @return PoDTO object with purchase order details
	 * @throws IllegalArgumentException if purchase order for given poCode dosen't exist.
	 */
	public PoDTO getOrder(int poCodeId) {
		Optional<PoDTO> order = getPoRepository().findOrderById(null, poCodeId);
		PoDTO po = order.orElseThrow(
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
		Optional<PoDTO> order = getPoRepository().findOrderById(processId, null);
		PoDTO po = order.orElseThrow(
				()->new IllegalArgumentException("No order with given process id"));
		po.setOrderItems(getPoRepository().findPoOrderItemsById(po.getId()));
		
		return po;
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
	
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeOrder(int orderId) {
		getDeletableDAO().permenentlyRemoveEntity(PO.class, orderId);
	}
	
}
