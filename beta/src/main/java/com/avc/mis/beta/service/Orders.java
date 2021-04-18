/**
 * 
 */
package com.avc.mis.beta.service;

import java.math.BigDecimal;
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
import com.avc.mis.beta.dto.basic.ValueObject;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.values.PoCodeDTO;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.dto.view.PoRow;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.MixPoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.SequenceIdentifier;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.utilities.ProgramSequence;

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
		List<PoItemRow> poItemRows = getOrdersByType(ProcessName.CASHEW_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true);
		return poItemRows;
	}
	
	/**
	 * Get the table of all Cashew purchase orders that are not cancelled.
	 * @return list of PoRow for all orders (not cancelled)
	 */
	public List<PoItemRow> findAllCashewOrderItems() {
		return getOrdersByType(ProcessName.CASHEW_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, false);
	}
	
	/**
	 * Get the table of all Cashew purchase orders that are not cancelled.
	 * @return list of PoRow for all orders (not cancelled)
	 */
	public List<PoItemRow> findAllGeneralOrderItems() {
		return getOrdersByType(ProcessName.GENERAL_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, false);
	}
	
	/**
	 * Get the table of all Cashew purchase orders including cancelled orders.
	 * @return list of PoRow for all orders
	 */
	public List<PoItemRow> findAllCashewOrderItemsHistory() {
		return getOrdersByType(ProcessName.CASHEW_ORDER, ProcessStatus.values(), null, null, false);
	}
	
	/**
	 * Get the table of all Cashew purchase orders including cancelled orders.
	 * @return list of PoRow for all orders
	 */
	public List<PoItemRow> findAllGeneralOrderItemsHistory() {
		return getOrdersByType(ProcessName.GENERAL_ORDER, ProcessStatus.values(), null, null, false);
	}
	
	public List<PoItemRow> getOrdersByType(ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId) {
		return getOrdersByType(orderType, processStatuses, poCodeId, null, false);
	}
	
	public List<PoItemRow> getOrdersByType(ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId, ItemGroup itemGroup, boolean onlyOpen) {
		List<PoItemRow> poItemRows = getPoRepository().findAllOrdersByType(orderType, processStatuses, poCodeId, itemGroup, onlyOpen);
		int[] orderItemIds = poItemRows.stream().mapToInt(PoItemRow::getOrderItemId).toArray();
		Map<Integer, BigDecimal> receivedAmountMap = getPoRepository()
				.findReceivedAmountByOrderItemIds(orderItemIds)
				.collect(Collectors.toMap(ValueObject<BigDecimal>::getId, ValueObject<BigDecimal>::getValue));
		Map<Integer, BigDecimal> receivedOrderUnitsMap = getPoRepository()
				.findReceivedOrderUnitsByOrderItemIds(orderItemIds)
				.collect(Collectors.toMap(ValueObject<BigDecimal>::getId, ValueObject<BigDecimal>::getValue));
		Map<Integer, Long> numReceiptsCancelledMap = getPoRepository()
				.findumReceiptsCancelledByOrderItemIds(orderItemIds)
				.collect(Collectors.toMap(ValueObject<Long>::getId, ValueObject<Long>::getValue));
		for(PoItemRow row: poItemRows) {
			row.setReceivedAmount(receivedAmountMap.get(row.getOrderItemId()));
			row.setReceivedOrderUnits(receivedOrderUnitsMap.get(row.getOrderItemId()));
			
			Optional<Long> numCancelled = Optional.ofNullable(numReceiptsCancelledMap.get(row.getOrderItemId()));
			row.setReceiptsCancelled(numCancelled.orElse(0L));
		}	
		return poItemRows;
	}
	
	/**
	 * Get the table of all General purchase orders that are active(not cancelled or archived) and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	public List<PoItemRow> findOpenGeneralOrderItems() {
		return getOrdersByType(ProcessName.GENERAL_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addPoCode(PoCode poCode) {
		dao.addEntity(poCode);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void addMixPoCode(MixPoCode poCode) {
		dao.addEntity(poCode);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addGeneralPoCode(GeneralPoCode poCode) {
		ProgramSequence sequence = dao.getSequnce(SequenceIdentifier.GENERAL_PO_CODE);
		poCode.setCode(String.valueOf(sequence.getSequance()));	
		sequence.advance();
		dao.addEntity(poCode);
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
		if(dao.isPoCodeFree(po.getPoCode().getId())) {
			dao.addGeneralProcessEntity(po);						
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
		addGeneralPoCode((GeneralPoCode) po.getPoCode());
				
		dao.addGeneralProcessEntity(po);						
	
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
	
	public PoCodeDTO getPoCode(int poCodeId) {
		Optional<PoCodeDTO> poCode = getPoRepository().findPoCodeById(poCodeId);
		return poCode.orElseThrow(
				()->new IllegalArgumentException("No PO code with given id"));		
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editPoCode(PoCode poCode) {
		dao.editEntity(poCode);
	}	
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editMixPoCode(MixPoCode poCode) {
		dao.editEntity(poCode);
	}	
	
	/**
	 * Get all cashew orders with the order status - pending, received, rejected but not cancelled.
	 * @return list of PoRow for all orders
	 */
//	@Deprecated
//	public List<PoRow> findAllCashewOrders() {
//		List<PoItemRow> itemRows = getPoRepository().findAllOrdersByType(ProcessName.CASHEW_ORDER,
//				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null);
//		return getPoRows(itemRows);
//	}
	
	/**
	 * Get all cashew orders with the order status - pending, received, rejected or cancelled.
	 * @return list of PoRow for all orders
	 */
//	@Deprecated
//	public List<PoRow> findAllCashewOrdersHistory() {
//		List<PoItemRow> itemRows = getPoRepository().findAllOrdersByType(ProcessName.CASHEW_ORDER, ProcessStatus.values(), null);
//		return getPoRows(itemRows);
//	}
	
	/**
	 * Gat all General orders with the order status - pending, received, rejected but not cancelled.
	 * @return list of PoRow for all orders
	 */
//	@Deprecated
//	public List<PoRow> findAllGeneralOrders() {
//		List<PoItemRow> itemRows = getPoRepository().findAllOrdersByType(ProcessName.GENERAL_ORDER,
//				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null);
//		return getPoRows(itemRows);
//	}
	
	/**
	 * Get the table of all Cashew purchase orders that are active and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	@Deprecated
	public List<PoRow> findOpenCashewOrders() {
		List<PoItemRow> itemRows = getOrdersByType(ProcessName.CASHEW_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true);
		
		return getPoRows(itemRows);
	}
	
	/**
	 * Get the table of all General purchase orders that are active and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	@Deprecated
	public List<PoRow> findOpenGeneralOrders() {
		List<PoItemRow> itemRows = getOrdersByType(ProcessName.GENERAL_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true);
		return getPoRows(itemRows);
	}

	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeOrder(int orderId) {
		getDeletableDAO().permenentlyRemoveEntity(PO.class, orderId);
	}
	
	
	
	
}
