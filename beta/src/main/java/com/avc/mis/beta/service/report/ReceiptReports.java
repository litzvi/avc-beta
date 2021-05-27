/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.view.ReceiptItemRow;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.repositories.ReceiptRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ReceiptReports {

	@Autowired private ReceiptRepository receiptRepository;	

	
	public List<ReceiptRow> findFinalCashewReceipts() {
		return findFinalCashewReceipts(null, null);
	}
	
	/**
	 * Gets rows for table of Cashew received orders that where finalized. 
	 * Contains all types of receipts including receipt without order.
	 * @return List of ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of finalized orders.
	 */
	public List<ReceiptRow> findFinalCashewReceipts(LocalDateTime startTime, LocalDateTime endTime) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.FINAL}, null, startTime, endTime);
	}

	public List<ReceiptRow> findFinalGeneralReceipts() {
		return findFinalGeneralReceipts(null, null);
	}
	
	/**
	 * Gets rows for table of General received orders. Contains all receipts including receipt without order.	 * 
	 * @return List of ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item.
	 */
	public List<ReceiptRow> findFinalGeneralReceipts(LocalDateTime startTime, LocalDateTime endTime) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.GENERAL_RECEIPT},
				new ProcessStatus[] {ProcessStatus.FINAL}, null, startTime, endTime);		
	}
	
	public List<ReceiptRow> findPendingCashewReceipts() {
		return findPendingCashewReceipts(null, null);
	}
	
	/**
	 * Gets rows for table of Cashew received orders that are still pending - where not finalized. 
	 * Contains all types of receipts including receipt without order.
	 * @return List of ReceiptRows that each contains a list of 
	 * ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of a pending - non finalized - order.
	 */
	public List<ReceiptRow> findPendingCashewReceipts(LocalDateTime startTime, LocalDateTime endTime) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.PENDING}, null, startTime, endTime);
		
	}
	
	public List<ReceiptRow> findPendingGeneralReceipts() {
		return findPendingGeneralReceipts(null, null);
	}
	
	/**
	 * Gets rows for table of General received orders that are still pending - where not finalized. 
	 * @return List of ReceiptRows that each contains a list of 
	 * ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of a pending - non finalized - order.
	 */
	public List<ReceiptRow> findPendingGeneralReceipts(LocalDateTime startTime, LocalDateTime endTime) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.GENERAL_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.PENDING}, null, startTime, endTime);
		
	}
	
	public List<ReceiptRow> findCancelledCashewReceipts() {
		return findCancelledCashewReceipts(null, null);
	}
	
	/**
	 * Gets rows for table of Cashew received orders that where cancelled. 
	 * Contains all types of receipts including receipt without order.
	 * @return List of ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of cancelled orders.
	 */
	public List<ReceiptRow> findCancelledCashewReceipts(LocalDateTime startTime, LocalDateTime endTime) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.CANCELLED}, null, startTime, endTime);
	}
	
	public List<ReceiptRow> findCashewReceiptsHistory() {
		return findCashewReceiptsHistory(null, null);
	}
	
	public List<ReceiptRow> findCashewReceiptsHistory (LocalDateTime startTime, LocalDateTime endTime) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				ProcessStatus.values(), null, startTime, endTime);
	}
	
	public List<ReceiptRow> findGeneralReceiptsHistory() {
		return findGeneralReceiptsHistory(null, null);
	}
	
	public List<ReceiptRow> findGeneralReceiptsHistory (LocalDateTime startTime, LocalDateTime endTime) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.GENERAL_RECEIPT}, 
				ProcessStatus.values(), null, startTime, endTime);
	}
	
	public List<ReceiptRow> findFinalCashewReceiptsByPoCode(@NonNull Integer poCodeId) {
		return findFinalCashewReceiptsByPoCode(poCodeId, null, null);
	}
	
	public List<ReceiptRow> findFinalCashewReceiptsByPoCode(@NonNull Integer poCodeId, LocalDateTime startTime, LocalDateTime endTime) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.FINAL}, 
				poCodeId, startTime, endTime);
	}
	
	public List<ReceiptRow> findAllReceiptsByType(ProcessName[] processNames, ProcessStatus[] statuses, Integer poCodeId) {
		return findAllReceiptsByType(processNames, statuses, poCodeId, null, null);
	}
	
	public List<ReceiptRow> findAllReceiptsByType(
			ProcessName[] processNames, ProcessStatus[] statuses, Integer poCodeId, 
			LocalDateTime startTime, LocalDateTime endTime) {
		List<ReceiptItemRow> itemRows = getReceiptRepository().findAllReceiptsByType(processNames, statuses, poCodeId, startTime, endTime);
		Map<Integer, List<ReceiptItemRow>> receiptMap = itemRows.stream()
				.collect(Collectors.groupingBy(ReceiptItemRow::getId, LinkedHashMap::new, Collectors.toList()));
		List<ReceiptRow> receiptRows = new ArrayList<ReceiptRow>();
		receiptMap.forEach((k, v) -> {
			ReceiptRow receiptRow = new ReceiptRow(k, v);
			receiptRows.add(receiptRow);
		});
		return receiptRows;
	}
	
}
