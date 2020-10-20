/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.view.ReceiptItemRow;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.processinfo.ExtraAdded;
import com.avc.mis.beta.entities.processinfo.OrderItem;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.repositories.ReceiptRepository;

import lombok.AccessLevel;
import lombok.Getter;

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
		
	
	/**
	 * Gets rows for table of Cashew received orders that where finalized. 
	 * Contains all types of receipts including receipt without order.
	 * @return List of ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item of finalized orders.
	 */
	public List<ReceiptRow> findFinalCashewReceipts() {
		return findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.FINAL});
	}

	/**
	 * Gets rows for table of General received orders. Contains all receipts including receipt without order.	 * 
	 * @return List of ReceiptItemRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item.
	 */
	public List<ReceiptItemRow> findFinalGeneralReceipts() {
		return getReceiptRepository().findAllReceiptsByType(
				new ProcessName[] {ProcessName.GENERAL_RECEIPT},
				new ProcessStatus[] {ProcessStatus.FINAL});		
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
				new ProcessStatus[] {ProcessStatus.PENDING});
		
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
				new ProcessStatus[] {ProcessStatus.PENDING});
		
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
				new ProcessStatus[] {ProcessStatus.CANCELLED});
	}
	
	private List<ReceiptRow> findAllReceiptsByType(ProcessName[] processNames, ProcessStatus[] statuses) {
		List<ReceiptItemRow> itemRows = getReceiptRepository().findAllReceiptsByType(
				processNames, statuses);
		Map<Integer, List<ReceiptItemRow>> receiptMap = itemRows.stream()
				.collect(Collectors.groupingBy(ReceiptItemRow::getId, Collectors.toList()));
		List<ReceiptRow> receiptRows = new ArrayList<ReceiptRow>();
		receiptMap.forEach((k, v) -> {
			AmountWithUnit totalAmount = v.stream()
					.map(pi -> pi.getReceiptAmount()[0])
					.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
			ReceiptRow receiptRow = new ReceiptRow(k, totalAmount, v);
			receiptRows.add(receiptRow);
		});
		receiptRows.sort(new Comparator<ReceiptRow>() {
			
			@Override
			public int compare(ReceiptRow o1, ReceiptRow o2) {
				return o1.getReceiptDate().compareTo(o2.getReceiptDate());
			}
		});
		return receiptRows;
	}
			
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addOrderReceipt(Receipt receipt) {
		//TODO should check if order item was already fully received(even if pending)
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
		// TODO Auto-generated method stub
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
		dao.addEntityWithFlexibleGenerator(receipt.getPoCode());
		addOrderReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_RECEIPT));
		addReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewOrderReceipt(Receipt receipt) {
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
	
	
		
	//maybe has a few
//	public ReceiptDTO getReceipt(int poCode) {
//		
//	}
	
	public ReceiptDTO getReceiptByProcessId(int processId) {
//		Optional<Receipt> result = getReceiptRepository().findReceiptByProcessId(processId);
//		Receipt receipt = result.orElseThrow(
//				()->new IllegalArgumentException("No order receipt with given process id"));
//		ReceiptDTO receiptDTO = new ReceiptDTO(receipt);
		Optional<ReceiptDTO> receipt = getReceiptRepository().findReceiptDTOByProcessId(processId);
		ReceiptDTO receiptDTO = receipt.orElseThrow(
				()->new IllegalArgumentException("No order receipt with given process id"));
		receiptDTO.setReceiptItems(getReceiptRepository().findReceiptItemWithStorage(processId));
		
		return receiptDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editReceipt(Receipt receipt) {
		//can't edit if finalised - should be checked by process status, perhaps in  table
		dao.editGeneralProcessEntity(receipt);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeReceipt(int receiptId) {
		getDeletableDAO().permenentlyRemoveEntity(Receipt.class, receiptId);
	}
}
