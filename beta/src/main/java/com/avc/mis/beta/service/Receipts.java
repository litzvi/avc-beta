/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.collection.ReceiptItemDTO;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.avc.mis.beta.entities.process.collection.ReceiptItem;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.repositories.ReceiptRepository;
import com.avc.mis.beta.service.reports.ReceiptReports;
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
	
	@Autowired private ReceiptReports receiptReports;
	@Autowired private ReceiptRepository receiptRepository;	
	@Autowired private PORepository poRepository;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_RECEIPT));
		addReceipt(receipt, PoCode.class);
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
		addReceipt(receipt, GeneralPoCode.class);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralOrderReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_RECEIPT));
		addOrderReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addOrderReceipt(Receipt receipt) {
		//checks if order item was already fully received(even if pending)
		if(!isOrderOpen(receipt.getReceiptItems())) {
			throw new IllegalArgumentException("Order items where already fully received");
		}
		dao.addPoProcessEntity(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private <T extends BasePoCode> void addReceipt(Receipt receipt, Class<T> clazz) {
		//using save rather than persist in case POid was assigned by user
//		dao.addEntityWithFlexibleGenerator(receipt.getPoCode());
//		addOrderReceipt(receipt);

		if(dao.isPoCodeFree(receipt.getPoCode().getId(), clazz)) {
			dao.addPoProcessEntity(receipt);						
		}
		else {
			throw new IllegalArgumentException("Po Code is already used for another order or receipt");
		}
		
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addExtra(ExtraAdded[] added, Integer receiptItemId) {
		Ordinal.setOrdinals(added);
		Arrays.stream(added).forEach(r -> dao.addEntity(r, ReceiptItem.class, receiptItemId));
	}
	
	public ReceiptDTO getReceiptByProcessId(int processId) {
		ReceiptDTO receiptDTO = new ReceiptDTO();
		receiptDTO.setGeneralProcessInfo(getReceiptRepository()
				.findGeneralProcessInfoByProcessId(processId, Receipt.class)
				.orElseThrow(
						()->new IllegalArgumentException("No order receipt with given process id")));
		receiptDTO.setPoProcessInfo(getReceiptRepository()
				.findPoProcessInfoByProcessId(processId, Receipt.class)
				.orElseThrow(
						()->new IllegalArgumentException("No po code for given process id")));
		
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
		dao.checkRemovingUsedProduct(receipt);		
		dao.editPoProcessEntity(receipt);
		dao.checkProducedInventorySufficiency(receipt);	
	}
	
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

	
	//----------------------------Duplicate in ReceiptReports - Should remove------------------------------------------
	
	public List<ReceiptRow> findFinalCashewReceipts() {
		return getReceiptReports().findFinalCashewReceipts();
	}

	public List<ReceiptRow> findFinalGeneralReceipts() {
		return getReceiptReports().findFinalGeneralReceipts();
	}
	
	public List<ReceiptRow> findPendingCashewReceipts() {
		return getReceiptReports().findPendingCashewReceipts();
	}
	
	public List<ReceiptRow> findPendingGeneralReceipts() {
		return getReceiptReports().findPendingGeneralReceipts();
	}
	
	public List<ReceiptRow> findCancelledCashewReceipts() {
		return getReceiptReports().findCancelledCashewReceipts();
	}
	
	public List<ReceiptRow> findCashewReceiptsHistory () {
		return getReceiptReports().findCashewReceiptsHistory();
	}
	
	public List<ReceiptRow> findGeneralReceiptsHistory () {
		return getReceiptReports().findGeneralReceiptsHistory();
	}
	
	public List<ReceiptRow> findFinalCashewReceiptsByPoCode(@NonNull Integer poCodeId) {
		return getReceiptReports().findFinalCashewReceiptsByPoCode(poCodeId);
	}
	
	public List<ReceiptRow> findAllReceiptsByType(ProcessName[] processNames, ProcessStatus[] statuses, Integer poCodeId) {
		return getReceiptReports().findAllReceiptsByType(processNames, statuses, poCodeId);
	}


}
