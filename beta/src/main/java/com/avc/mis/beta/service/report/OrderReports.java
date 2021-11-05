/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.generic.ValueObject;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.repositories.PORepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class OrderReports {
	
	@Autowired private PORepository poRepository;	

	public List<PoItemRow> findOpenCashewOrderItems() {
		return findOpenCashewOrderItems(null, null);
	}

	/**
	 * Get the table of all Cashew purchase orders that are active(not cancelled or archived) and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	public List<PoItemRow> findOpenCashewOrderItems(LocalDate startTime, LocalDate endTime) {
		List<PoItemRow> poItemRows = getOrdersByType(ProcessName.CASHEW_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true, startTime, endTime);
		return poItemRows;
	}
	
	public List<PoItemRow> findAllCashewOrderItems() {
		return findAllCashewOrderItems(null, null);
	}
	
	/**
	 * Get the table of all Cashew purchase orders that are not cancelled.
	 * @return list of PoRow for all orders (not cancelled)
	 */
	public List<PoItemRow> findAllCashewOrderItems(LocalDate startTime, LocalDate endTime) {
		return getOrdersByType(ProcessName.CASHEW_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, false, startTime, endTime);
	}

	public List<PoItemRow> findAllGeneralOrderItems() {
		return findAllGeneralOrderItems(null, null);
	}
	
	/**
	 * Get the table of all Cashew purchase orders that are not cancelled.
	 * @return list of PoRow for all orders (not cancelled)
	 */
	public List<PoItemRow> findAllGeneralOrderItems(LocalDate startTime, LocalDate endTime) {
		return getOrdersByType(ProcessName.GENERAL_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, false, startTime, endTime);
	}
	
	public List<PoItemRow> findAllCashewOrderItemsHistory() {
		return findAllCashewOrderItemsHistory(null, null);
	}
	
	/**
	 * Get the table of all Cashew purchase orders including cancelled orders.
	 * @return list of PoRow for all orders
	 */
	public List<PoItemRow> findAllCashewOrderItemsHistory(LocalDate startTime, LocalDate endTime) {
		return getOrdersByType(ProcessName.CASHEW_ORDER, ProcessStatus.values(), null, null, false, startTime, endTime);
	}
	
	public List<PoItemRow> findAllGeneralOrderItemsHistory() {
		return findAllGeneralOrderItemsHistory(null, null);
	}
	
	/**
	 * Get the table of all Cashew purchase orders including cancelled orders.
	 * @return list of PoRow for all orders
	 */
	public List<PoItemRow> findAllGeneralOrderItemsHistory(LocalDate startTime, LocalDate endTime) {
		return getOrdersByType(ProcessName.GENERAL_ORDER, ProcessStatus.values(), null, null, false, startTime, endTime);
	}
	
	public List<PoItemRow> getOrdersByType(ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId) {
		return getOrdersByType(orderType, processStatuses, poCodeId, null, null);
	}
	
	public List<PoItemRow> getOrdersByType(ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId,
			LocalDate startTime, LocalDate endTime) {
		return getOrdersByType(orderType, processStatuses, poCodeId, null, false, startTime, endTime);
	}
	
	public List<PoItemRow> findOpenGeneralOrderItems() {
		return findOpenGeneralOrderItems(null, null);
	}
		
	/**
	 * Get the table of all General purchase orders that are active(not cancelled or archived) and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	public List<PoItemRow> findOpenGeneralOrderItems(LocalDate startTime, LocalDate endTime) {
		return getOrdersByType(ProcessName.GENERAL_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true, startTime, endTime);
	}
	
	public List<PoItemRow> getOrdersByType(
			ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId, ItemGroup itemGroup, boolean onlyOpen) {
		return getOrdersByType(orderType, processStatuses, poCodeId, itemGroup, onlyOpen, null, null);
	}
	
	public List<PoItemRow> getOrdersByType(
			ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId, ItemGroup itemGroup, boolean onlyOpen, 
			LocalDate startTime, LocalDate endTime) {
		List<PoItemRow> poItemRows = getPoRepository().findAllOrdersByType(orderType, processStatuses, poCodeId, itemGroup, onlyOpen, startTime, endTime);
		int[] orderItemIds = poItemRows.stream().mapToInt(PoItemRow::getOrderItemId).toArray();
		Map<Integer, BigDecimal> receivedAmountMap = getPoRepository()
				.findReceivedAmountByOrderItemIds(orderItemIds)
//				.collect(HashMap::new, (m,v)->m.put(v.getId(), v.getValue()), HashMap::putAll); //Allows null values
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
	
	
	
}
