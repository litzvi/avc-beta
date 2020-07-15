/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.processinfo.ExtraAdded;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Suppliers;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class ReceiptTest {

	@Autowired TestService service;	
	@Autowired Receipts receipts;	
	@Autowired Orders orders;	
	@Autowired Suppliers suppliers;
	@Autowired ProcessInfoWriter processInfoWriter;
	
//	@Disabled
	@Test
	void receiptTest() {
		//insert order receipt without order
		Receipt receipt = service.addBasicCashewReceipt();
		ReceiptDTO expected;
		expected = new ReceiptDTO(receipt);
		ReceiptDTO actual = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(expected, actual, "failed test adding receipt without order");
		service.cleanup(receipt);

		//insert order receipt for order
		PO po = service.addBasicCashewOrder();
		receipt = service.getCashewOrderReceipt(po.getPoCode().getId());
		expected = new ReceiptDTO(receipt);
		actual = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(expected, actual, "failed test adding order receipt");
		
		//change receipt process life cycle to lock process for editing
		processInfoWriter.setEditStatus(EditStatus.LOCKED, po.getId());
				
		//add extra bonus
		ExtraAdded[] added = new ExtraAdded[1];
		added[0] = new ExtraAdded();
		added[0].setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(500), "KG"));//because database is set to scale 2
		added[0].setNumberUnits(new BigDecimal(4).setScale(2));
		receipts.addExtra(added, receipt.getProcessItems()[0].getId());
		receipt.getProcessItems()[0]
				.setStorageForms(ArrayUtils.addAll(receipt.getProcessItems()[0].getStorageForms(), added));
		expected = new ReceiptDTO(receipt);
		actual = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(expected, actual, "failed test adding extra bonus");
		service.cleanup(receipt);		
		service.cleanup(po);		
	}



}
