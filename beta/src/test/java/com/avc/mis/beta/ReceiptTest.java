/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.storages.ExtraAddedDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
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

	@Autowired
	TestService service;
	@Autowired
	Receipts receipts;
	@Autowired
	Orders orders;
	@Autowired
	Suppliers suppliers;
	@Autowired
	ProcessInfoWriter processInfoWriter;

//	@Disabled
	@Test
	void receiptTest() {
		// insert order receipt without order
		ReceiptDTO receipt;
		try {
			receipt = service.addBasicCashewReceipt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		ReceiptDTO expected;
		expected = receipt;
		ReceiptDTO actualReceipt = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(expected, actualReceipt, "failed test adding receipt without order");
		service.cleanup(receipt);

		// insert order receipt for order
		PoDTO po;
		try {
			po = service.addBasicCashewOrder();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		System.out.println("po code: " + po.getPoCode().getId());
		try {
			receipt = service.getCashewOrderReceipt(po.getPoCode().getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		expected = receipt;
		actualReceipt = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(expected, actualReceipt, "failed test adding order receipt");

		// change receipt process life cycle to lock process for editing
		processInfoWriter.setEditStatus(EditStatus.LOCKED, po.getId());

		// add extra bonus
		List<ExtraAddedDTO> added = new ArrayList<>();
		ExtraAddedDTO add = new ExtraAddedDTO();
		added.add(add);
		add.setUnitAmount(BigDecimal.valueOf(500));// because database is set to scale 2
		add.setNumberUnits(new BigDecimal(4).setScale(2));
		receipts.addExtra(added, actualReceipt.getReceiptItems().get(0).getId());
		receipt.getReceiptItems().get(0).setExtraAdded(added);
		;
//		receipt.getReceiptItems()[0]
//				.setStorageForms(ArrayUtils.addAll(receipt.getReceiptItems()[0].getStorageForms(), added));
		expected = receipt;
		actualReceipt = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(expected, actualReceipt, "failed test adding extra bonus");
		service.cleanup(receipt);
		service.cleanup(po);
	}
}
