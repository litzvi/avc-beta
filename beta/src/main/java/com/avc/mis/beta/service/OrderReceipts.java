/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.values.ReceiptRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ExtraAdded;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ReceiptItem;
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
public class OrderReceipts {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private ReceiptRepository receiptRepository;	
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
		
	
	/**
	 * Gets rows for table of Cashew received orders. Contains all receipts including receipt without order.	 * 
	 * @return List of ReceiptRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item.
	 */
	public List<ReceiptRow> findCashewReceipts() {
		return getReceiptRepository().findAllReceiptsByType(
				new ProcessName[] {ProcessName.CASHEW_RECEIPT, ProcessName.CASHEW_ORDER_RECEIPT});
	}
	
	/**
	 * Gets rows for table of General received orders. Contains all receipts including receipt without order.	 * 
	 * @return List of ReceiptRow - id, PO#, supplier, item, order amount, receipt amount,
	 * receipt date and storage - for every received item.
	 */
	public List<ReceiptRow> findGeneralReceipts() {
		return getReceiptRepository().findAllReceiptsByType(new ProcessName[] {ProcessName.GENERAL_RECEIPT});		
	}
		
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addOrderReceipt(Receipt receipt) {
		dao.addProcessEntity(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addReceipt(Receipt receipt) {
		//using save rather than persist in case POid was assigned by user
		dao.addEntityWithFlexibleGenerator(receipt.getPoCode());
		dao.addProcessEntity(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_RECEIPT));
		addReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewOrderReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_ORDER_RECEIPT));
		addOrderReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addExtra(ExtraAdded added, Integer receiptItemId) {
		ReceiptItem receiptItem = new ReceiptItem();
		receiptItem.setId(receiptItemId);
		dao.addEntity(added, receiptItem);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editExtra(ExtraAdded added) {
		dao.editEntity(added);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralReceipt(Receipt receipt) {
		receipt.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_RECEIPT));
		addReceipt(receipt);
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
		dao.editProcessEntity(receipt);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeReceipt(int receiptId) {
		getDeletableDAO().permenentlyRemoveEntity(Receipt.class, receiptId);
	}
}
