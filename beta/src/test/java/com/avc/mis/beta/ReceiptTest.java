/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.ProcessItem;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ReceiptItem;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Storage;
import com.avc.mis.beta.service.OrderReceipts;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WithUserDetails("eli")
public class ReceiptTest {
	
	@Autowired OrderReceipts receipts;

	private Receipt basicReceipt() {
		//build order receipt
		Receipt receipt = new Receipt();
		PoCode poCode = new PoCode();
		poCode.setCode(OrdersTest.PROCESS_NO);
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(OffsetDateTime.now());
		//add order items
		ProcessItem[] items = processItems(OrdersTest.NUM_ITEMS);				
		receipt.setProcessItems(items);
		return receipt;
	}
	
	private ReceiptItem[] processItems(int numOfItems) {
		ReceiptItem[] items = new ReceiptItem[numOfItems];
		Item item = new Item();
		item.setId(1);
		Storage storage = new Storage();
		storage.setId(1);
		OrderItem orderItem = new OrderItem();
		orderItem.setId(96);
		orderItem.setVersion(0);
		for(int i=0; i<items.length; i++) {
			items[i] = new ReceiptItem();
			items[i].setItem(item);
			items[i].setUnitAmount(BigDecimal.valueOf(10, 2));//because database is set to scale 2
			items[i].setMeasureUnit("KG");
			items[i].setNumberUnits(new BigDecimal(i).setScale(2));
			items[i].setStorageLocation(storage);
			items[i].setOrderItem(orderItem);
		}
//		Arrays.stream(items).forEach(i -> System.out.println(i));
		return items;
	}
	
//	@Disabled
	@Test
	void receiptTest() {
		//insert order receipt
		Receipt receipt = basicReceipt();
		receipts.addCashewReceipt(receipt);
		ReceiptDTO expected = new ReceiptDTO(receipt);
		ReceiptDTO actual = receipts.getReceiptByProcessId(receipt.getId());
		System.out.println(actual);
		assertEquals(expected, actual, "failed test adding order receipt");

		//
		
	}
}
