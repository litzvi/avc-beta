/**
 * 
 */
package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.entities.enums.ProcessName;
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
	
	
//	public List<ReceiptRow> findCashewReceipts() {
//		
//	}
//	
//	public List<ReceiptRow> findGeneralReceipts() {
//		
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
	
//	public ReceiptDTO getReceiptByProcessId(int processId) {
//		Optional<ReceiptDTO> receipt = getReceiptRepository().findReceiptByProcessId(processId);
//		ReceiptDTO receiptDTO = receipt.orElseThrow(
//				()->new IllegalArgumentException("No order receipt with given process id"));
//		receiptDTO.setProcessItems(getReceiptRepository().findProcessItemsById(processId));
//		
//		return receiptDTO;
//	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editReceipt(Receipt receipt) {
		dao.editProcessEntity(receipt);
	}

}
