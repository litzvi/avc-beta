/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.embedable.PoProcessInfo;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.processinfo.ReceiptItemDTO;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ProcessStateInfo;
import com.avc.mis.beta.dto.report.ReceiptReportLine;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ReceiptItemRow;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.entities.processinfo.OrderItem;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.repositories.ReceiptRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class Receipts {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private ReceiptRepository receiptRepository;	
	@Autowired private PORepository poRepository;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
		
	public ReceiptReportLine getReceiptSummary(Integer poCodeId) {
//		List<ProcessRow> processRows = getReceiptRepository().findProcessByType(ProcessName.CASHEW_RECEIPT, poCodeId, false);
		List<ProcessStateInfo> processes = getReceiptRepository().findProcessReportLines(ProcessName.CASHEW_RECEIPT, poCodeId, false);
		int[] processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();

		if(processes.isEmpty()) {
			return null;
		}
		
		ReceiptReportLine reportLine = new ReceiptReportLine();
//		reportLine.setReceivedOrderUnits(getReceiptRepository().);
//		reportLine.setPoCode(processRows.get(0).getPoCode());
//		reportLine.setSupplierName(processRows.get(0).getSupplierName());
		reportLine.setProcesses(processes);
//		reportLine.setDates(processRows.stream().map(r -> r.getRecordedTime().toLocalDate()).collect(Collectors.toSet()));
		
		Stream<ItemAmount> itemAmounts = getReceiptRepository().findSummaryProducedItemAmounts(processIds, poCodeId);
		reportLine.setReceived(itemAmounts.collect(Collectors.toList()));
		
		List<ItemAmount> countAmounts = null;
		if(reportLine.getReceived() != null) {
			int[] productItemsIds = reportLine.getReceived().stream().mapToInt(i -> i.getItem().getId()).toArray();
			processes = getReceiptRepository().findProcessReportLines(ProcessName.STORAGE_RELOCATION, poCodeId, false);
			processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();
			for(int i=0; i < processIds.length && (countAmounts == null || countAmounts.isEmpty()); i++) {
				countAmounts = getReceiptRepository().findProductCountItemAmountsByProcessId(processIds[i], productItemsIds);
			}
			reportLine.setProductCount(countAmounts);						
		}

		return reportLine;
	}
	
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
			
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addOrderReceipt(Receipt receipt) {
		//checks if order item was already fully received(even if pending)
		if(!isOrderOpen(receipt.getReceiptItems())) {
			throw new IllegalArgumentException("Order items where already fully received");
		}
		dao.addGeneralProcessEntity(receipt);
	}
	
	
	/**
	 * @param receiptItems
	 * @return
	 */
	private boolean isOrderOpen(ReceiptItem[] receiptItems) {
		if(receiptItems == null || receiptItems.length == 0) {
			return true;
		}
		List<OrderItem> orderItems = getPoRepository().findNonOpenOrderItemsById(
				Stream.of(receiptItems)
				.filter(i -> i.getOrderItem() != null)
				.map(i -> i.getOrderItem().getId())
				.toArray(Integer[]::new));
		
		return orderItems.isEmpty();
		
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addReceipt(Receipt receipt) {
		//using save rather than persist in case POid was assigned by user
//		dao.addEntityWithFlexibleGenerator(receipt.getPoCode());
//		addOrderReceipt(receipt);

		if(dao.isPoCodeFree(receipt.getPoCode().getId())) {
			dao.addGeneralProcessEntity(receipt);						
		}
		else {
			throw new IllegalArgumentException("Po Code is already used for another order or receipt");
		}
		
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_RECEIPT));
		addReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewOrderReceipt(Receipt receipt) {
		//check that there wasn't another receipt for the same po
		if(dao.isPoCodeReceived(receipt.getPoCode().getId())) {
			throw new IllegalArgumentException("Po Code of Product can only be received once, "
					+ "in order to correctly reference receipt date by po code");
		}
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_RECEIPT));
		addOrderReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_RECEIPT));
		addReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralOrderReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_RECEIPT));
		addOrderReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addExtra(ExtraAdded[] added, Integer receiptItemId) {
//		ReceiptItem receiptItem = new ReceiptItem();
//		receiptItem.setId(receiptItemId);
		Ordinal.setOrdinals(added);
		Arrays.stream(added).forEach(r -> dao.addEntity(r, ReceiptItem.class, receiptItemId));
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated //since add extra is done even after finalize and considered in stock, shouldn't be amended after insert.
	public void editExtra(ExtraAdded[] added) {
		Ordinal.setOrdinals(added);
		Arrays.stream(added).forEach(r -> dao.addEntity(r));
	}
	
	public ReceiptDTO getReceiptByProcessId(int processId) {
		ReceiptDTO receiptDTO = new ReceiptDTO();
		receiptDTO.setGeneralProcessInfo(getReceiptRepository()
				.findGeneralProcessInfoByProcessId(processId, Receipt.class)
				.orElseThrow(
						()->new IllegalArgumentException("No order receipt with given process id")));
		receiptDTO.setPoProcessInfo(getReceiptRepository()
				.findPoProcessInfoByProcessId(processId)
				.orElseThrow(
						()->new IllegalArgumentException("No po code for given process id")));
		
//		Optional<ReceiptDTO> receipt = getReceiptRepository().findReceiptDTOByProcessId(processId);
//		ReceiptDTO receiptDTO = receipt.orElseThrow(
//				()->new IllegalArgumentException("No order receipt with given process id"));
		receiptDTO.setReceiptItems(
				CollectionItemWithGroup.getFilledGroups(
						getReceiptRepository()
						.findReceiptItemWithStorage(processId))
				.stream().map(i -> (ReceiptItemDTO)i).collect(Collectors.toList())
				);
		
		return receiptDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editReceipt(Receipt receipt) {
		//can't edit if finalised - should be checked by process status, perhaps in  table
		dao.editProcessWithProductEntity(receipt);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeReceipt(int receiptId) {
		getDeletableDAO().permenentlyRemoveEntity(Receipt.class, receiptId);
	}
}
