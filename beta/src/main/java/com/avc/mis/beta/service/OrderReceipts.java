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
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
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
		
	
	public List<ReceiptRow> findCashewReceipts() {
		return getReceiptRepository().findCashewReceiptByType();
	}
	
//	public List<ReceiptRow> findGeneralReceipts() {
//		return getReceiptRepository().findReceiptByType(ProcessName.GENERAL_RECEIPT);		
//	}
	
	/**
	 * @param receipt
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addReceipt(Receipt receipt) {
		dao.addProcessEntity(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewReceipt(Receipt receipt) {
		receipt.setProcessType(getReceiptRepository().findProcessTypeByValue(ProcessName.CASHEW_RECEIPT));
		addReceipt(receipt);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralReceipt(Receipt receipt) {
		receipt.setProcessType(getReceiptRepository().findProcessTypeByValue(ProcessName.GENERAL_RECEIPT));
		addReceipt(receipt);
	}
	
	//maybe has a few
//	public ReceiptDTO getReceipt(int poCode) {
//		
//	}
	
	public ReceiptDTO getReceiptByProcessId(int processId) {
		Optional<ReceiptDTO> receipt = getReceiptRepository().findReceiptByProcessId(processId);
		ReceiptDTO receiptDTO = receipt.orElseThrow(
				()->new IllegalArgumentException("No order receipt with given process id"));
		System.out.println(processId);
		receiptDTO.setProcessItems(getReceiptRepository().findProcessItemsById(processId));
		
		return receiptDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editReceipt(Receipt receipt) {
		dao.editProcessEntity(receipt);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeReceipt(int receiptId) {
		getDeletableDAO().permenentlyRemoveEntity(Receipt.class, receiptId);
	}
}
