/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessDAO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.group.ReceiptItemDTO;
import com.avc.mis.beta.dto.process.storages.ExtraAddedDTO;
import com.avc.mis.beta.dto.query.ReceiptItemWithStorage;
import com.avc.mis.beta.dto.view.ReceiptRow;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.collectionItems.OrderItem;
import com.avc.mis.beta.entities.process.group.ReceiptItem;
import com.avc.mis.beta.entities.process.storages.ExtraAdded;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.repositories.ReceiptRepository;
import com.avc.mis.beta.service.report.ReceiptReports;
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
	
	@Autowired private ProcessDAO dao;
	@Autowired private DeletableDAO deletableDAO;
	
	@Autowired private ReceiptReports receiptReports;
	@Autowired private ReceiptRepository receiptRepository;	
	@Autowired private PORepository poRepository;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addCashewReceipt(ReceiptDTO receipt) {
		receipt.setProcessName(ProcessName.CASHEW_RECEIPT);
		return addReceipt(receipt, ProductPoCode.class);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addCashewOrderReceipt(ReceiptDTO receipt) {
		receipt.setProcessName(ProcessName.CASHEW_RECEIPT);
		return addOrderReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addGeneralReceipt(ReceiptDTO receipt) {
		receipt.setProcessName(ProcessName.GENERAL_RECEIPT);
		return addReceipt(receipt, GeneralPoCode.class);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addGeneralOrderReceipt(ReceiptDTO receipt) {
		receipt.setProcessName(ProcessName.GENERAL_RECEIPT);
		return addOrderReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	private Integer addOrderReceipt(ReceiptDTO receipt) {
		//checks if order item was already fully received(even if pending)
		if(!isOrderOpen(receipt.getReceiptItems())) {
			throw new IllegalArgumentException("Order items where already fully received");
		}
		return dao.addPoProcessEntity(receipt, Receipt::new);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	private <T extends BasePoCode> Integer addReceipt(ReceiptDTO receipt, Class<T> clazz) {
		//using save rather than persist in case POid was assigned by user
//		dao.addEntityWithFlexibleGenerator(receipt.getPoCode());
//		addOrderReceipt(receipt);
		if(dao.isPoCodeFree(receipt.getPoCode().getId(), clazz)) {
			return dao.addPoProcessEntity(receipt, Receipt::new);						
		}
		else {
			throw new IllegalArgumentException("Po Code is already used for another order or receipt");
		}
		
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void addExtra(List<ExtraAddedDTO> added, Integer receiptItemId) {
		List<Integer> previousAddedIds = getReceiptRepository().findAddedByReceiptItem(receiptItemId);
		Ordinal.setOrdinals(added);
		added.forEach(r -> {
			if(r.getId() != null) {
				previousAddedIds.remove(r.getId());
				dao.editEntity(r.fillEntity(new ExtraAdded()));
			}
			else {
				dao.addEntity(r.fillEntity(new ExtraAdded()), ReceiptItem.class, receiptItemId);
			}			
		});
		previousAddedIds.forEach(id -> deletableDAO.permenentlyRemoveEntity(ExtraAdded.class, id));
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
						getReceiptRepository().findReceiptItemWithStorage(processId), 
						ReceiptItemWithStorage::getReceiptItem, 
						ReceiptItemWithStorage:: getStorage,
						ReceiptItemDTO::setStorageWithSamples)
				.stream().map(i -> (ReceiptItemDTO)i).collect(Collectors.toList())
				);
		
		return receiptDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void editReceipt(ReceiptDTO receipt) {
		dao.checkRemovingUsedProduct(receipt);		
		dao.editPoProcessEntity(receipt, Receipt::new);
		dao.checkUsingProcesessConsistency(receipt);	
	}
	
	private boolean isOrderOpen(List<ReceiptItemDTO> receiptItems) {
		if(receiptItems == null || receiptItems.size() == 0) {
			return true;
		}
		List<OrderItem> orderItems = getPoRepository().findNonOpenOrderItemsById(
				receiptItems.stream()
				.filter(i -> i.getOrderItem() != null)
				.map(i -> i.getOrderItem().getId())
				.toArray(Integer[]::new));
		
		return orderItems.isEmpty();
		
	}

	
	//----------------------------Duplicate in ReceiptReports - Should remove------------------------------------------
	
	@Deprecated
	public List<ReceiptRow> findFinalCashewReceipts() {
		return getReceiptReports().findFinalCashewReceipts();
	}

	@Deprecated
	public List<ReceiptRow> findFinalGeneralReceipts() {
		return getReceiptReports().findFinalGeneralReceipts();
	}
	
	@Deprecated
	public List<ReceiptRow> findPendingCashewReceipts() {
		return getReceiptReports().findPendingCashewReceipts();
	}
	
	@Deprecated
	public List<ReceiptRow> findPendingGeneralReceipts() {
		return getReceiptReports().findPendingGeneralReceipts();
	}
	
	@Deprecated
	public List<ReceiptRow> findCancelledCashewReceipts() {
		return getReceiptReports().findCancelledCashewReceipts();
	}
	
	@Deprecated
	public List<ReceiptRow> findCashewReceiptsHistory () {
		return getReceiptReports().findCashewReceiptsHistory();
	}
	
	@Deprecated
	public List<ReceiptRow> findGeneralReceiptsHistory () {
		return getReceiptReports().findGeneralReceiptsHistory();
	}
	
	@Deprecated
	public List<ReceiptRow> findFinalCashewReceiptsByPoCode(@NonNull Integer poCodeId) {
		return getReceiptReports().findFinalCashewReceiptsByPoCode(poCodeId);
	}
	
	@Deprecated
	public List<ReceiptRow> findAllReceiptsByType(ProcessName[] processNames, ProcessStatus[] statuses, Integer poCodeId) {
		return getReceiptReports().findAllReceiptsByType(processNames, statuses, poCodeId);
	}


}
