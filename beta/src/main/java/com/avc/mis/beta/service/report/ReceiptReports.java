/**
 * 
 */
package com.avc.mis.beta.service.report;

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

	/**
	 * Gets rows for table of Cashew received orders that where finalized. 
	 * Contains all types of receipts including receipt without order.
	 * @return List of ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of finalized orders.
	 */
	public List<ReceiptRow> findFinalCashewReceipts() {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.FINAL}, null);
	}

	/**
	 * Gets rows for table of General received orders. Contains all receipts including receipt without order.	 * 
	 * @return List of ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item.
	 */
	public List<ReceiptRow> findFinalGeneralReceipts() {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.GENERAL_RECEIPT},
				new ProcessStatus[] {ProcessStatus.FINAL}, null);		
	}
	
	/**
	 * Gets rows for table of Cashew received orders that are still pending - where not finalized. 
	 * Contains all types of receipts including receipt without order.
	 * @return List of ReceiptRows that each contains a list of 
	 * ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of a pending - non finalized - order.
	 */
	public List<ReceiptRow> findPendingCashewReceipts() {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.PENDING}, null);
		
	}
	
	/**
	 * Gets rows for table of General received orders that are still pending - where not finalized. 
	 * @return List of ReceiptRows that each contains a list of 
	 * ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of a pending - non finalized - order.
	 */
	public List<ReceiptRow> findPendingGeneralReceipts() {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.GENERAL_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.PENDING}, null);
		
	}
	
	/**
	 * Gets rows for table of Cashew received orders that where cancelled. 
	 * Contains all types of receipts including receipt without order.
	 * @return List of ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of cancelled orders.
	 */
	public List<ReceiptRow> findCancelledCashewReceipts() {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.CANCELLED}, null);
	}
	
	public List<ReceiptRow> findCashewReceiptsHistory () {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				ProcessStatus.values(), null);
	}
	
	public List<ReceiptRow> findGeneralReceiptsHistory () {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.GENERAL_RECEIPT}, 
				ProcessStatus.values(), null);
	}
	
	public List<ReceiptRow> findFinalCashewReceiptsByPoCode(@NonNull Integer poCodeId) {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.FINAL}, 
				poCodeId);
	}
	
	public List<ReceiptRow> findAllReceiptsByType(ProcessName[] processNames, ProcessStatus[] statuses, Integer poCodeId) {
		List<ReceiptItemRow> itemRows = getReceiptRepository().findAllReceiptsByType(processNames, statuses, poCodeId);
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
