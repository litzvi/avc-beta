/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.math.BigDecimal;
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
		
	/**
	 * Get the table of all General purchase orders that are active(not cancelled or archived) and where not received.
	 * @return list of PoRow for orders that are yet to be received
	 */
	public List<PoItemRow> findOpenGeneralOrderItems() {
		return getOrdersByType(ProcessName.GENERAL_ORDER, 
				new ProcessStatus[] {ProcessStatus.FINAL, ProcessStatus.PENDING}, null, null, true);
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
	
	
	
}
