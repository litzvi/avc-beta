/**
 * 
 */
package com.avc.mis.beta;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ReceiptItem;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Storage;
import com.avc.mis.beta.repositories.ValueTablesRepository;
import com.avc.mis.beta.service.OrderReceipts;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class GeneralTest {
	
	static final Integer PO_CODE = 800001;
	static final Integer NUM_PO_ITEMS = 2;
	
	@Autowired ValueTablesRepository valueTablesRepository;

	
	@Autowired ValueTablesReader valueTablesReader;
	@Autowired Suppliers suppliers;
	@Autowired Orders orders;
	@Autowired OrderReceipts receipts;
	
	@Test
	void orderAndReceiveTest() {
		//create basic supplier with all existing supply categories
		Supplier supplier = new Supplier();
		supplier.setName("Test General supplier2");
		supplier.setSupplyCategories(valueTablesReader.getAllSupplyCategories().stream().collect(Collectors.toSet()));
		suppliers.addSupplier(supplier);
		SupplierDTO supplierDTO = suppliers.getSupplier(supplier.getId());
		assertEquals(new SupplierDTO(supplier), supplierDTO, "Supplier not added or fetched correctly");
		
		//create a cashew order with 2 order lines
		PO po = new PO();
		PoCode poCode = new PoCode();
		poCode.setId(PO_CODE);
		poCode.setContractType(valueTablesRepository.findContractTypeByValue(ContractTypeCode.VAT));
		po.setPoCode(poCode);
		po.setSupplier(supplier);
		po.setRecordedTime(OffsetDateTime.now());
		OrderItem[] orderItems = new OrderItem[NUM_PO_ITEMS];
		List<Item> items = valueTablesReader.getAllItems();
		for(int i=0; i < NUM_PO_ITEMS; i++) {
			orderItems[i] = new OrderItem();
			orderItems[i].setItem(items.get(i));
			orderItems[i].setNumberUnits(new BigDecimal(35000));
			orderItems[i].setCurrency("USD");
			orderItems[i].setMeasureUnit("LBS");
			orderItems[i].setUnitPrice(new BigDecimal("2.99"));
			orderItems[i].setDeliveryDate(LocalDate.now().toString());			
		}
		po.setOrderItems(orderItems);
		orders.addCashewOrder(po);
		PoDTO poDTO = orders.getOrderByProcessId(po.getId());
		assertEquals(new PoDTO(po), poDTO, "PO not added or fetched correctly");
		
		//receive both order lines in parts and different storages
		Receipt receipt = new Receipt();
		receipt.setPoCode(poCode);
		receipt.setRecordedTime(OffsetDateTime.now());
		receipt.setSupplier(supplier);
		ReceiptItem[] receiptItems = new ReceiptItem[NUM_PO_ITEMS*2];
		List<Storage> storages = valueTablesReader.getAllStorages();
		for(int i=0; i < receiptItems.length; i++) {
			receiptItems[i] = new ReceiptItem();
			receiptItems[i].setItem(orderItems[i/2].getItem());
			receiptItems[i].setStorageLocation(storages.get(i/2));
			receiptItems[i].setMeasureUnit("KG");
			receiptItems[i].setOrderItem(orderItems[i/2]);			
		}
		for(int i=0; i < NUM_PO_ITEMS; i++) {
			receiptItems[2*i].setUnitAmount(BigDecimal.valueOf(50));
			receiptItems[2*i].setNumberUnits(BigDecimal.valueOf(326));
			
			receiptItems[2*i+1].setUnitAmount(BigDecimal.valueOf(1));
			receiptItems[2*i+1].setNumberUnits(BigDecimal.valueOf(26));
				
		}
		receipt.setProcessItems(receiptItems);
		receipts.addCashewReceipt(receipt);
		ReceiptDTO receiptDTO = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(new ReceiptDTO(receipt), receiptDTO, "Order Receipt not added or fetched correctly");
		
		//print all
		System.out.println("Supplier: " + supplierDTO);
		System.out.println("Purchase Order: " + poDTO);
		System.out.println("Order receipt: " + receiptDTO);
		
		//remove all
		receipts.removeReceipt(receiptDTO.getId());
		orders.removeOrder(poDTO.getId());
		suppliers.permenentlyRemoveSupplier(supplierDTO.getId());

	}
}
