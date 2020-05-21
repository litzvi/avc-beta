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

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.values.ReceiptRow;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.process.RawItemQuality;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ReceiptItem;
import com.avc.mis.beta.entities.process.Storage;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.repositories.ValueTablesRepository;
import com.avc.mis.beta.service.OrderReceipts;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.QualityChecks;
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
	
	static final Integer PO_CODE = 800016;
	static final Integer NUM_PO_ITEMS = 2;
	static final Integer NUM_OF_CHECKS = 1;
	
	@Autowired private DeletableDAO deletableDAO;
	
	@Autowired ValueTablesRepository valueTablesRepository;

	
	@Autowired ValueTablesReader valueTablesReader;
	@Autowired Suppliers suppliers;
	@Autowired Orders orders;
	@Autowired OrderReceipts receipts;
	@Autowired QualityChecks checks;
	
	@Test
	void orderAndReceiveTest() {
		//create basic supplier with all existing supply categories
		Supplier supplier = new Supplier();
		supplier.setName("Test supplier" + PO_CODE);
		supplier.setSupplyCategories(valueTablesReader.getAllSupplyCategories().stream().collect(Collectors.toSet()));
		suppliers.addSupplier(supplier);
		SupplierDTO supplierDTO = suppliers.getSupplier(supplier.getId());
		assertEquals(new SupplierDTO(supplier), supplierDTO, "Supplier not added or fetched correctly");
		
		//create a cashew order with 2 order lines
		PO po = new PO();
		PoCode poCode = new PoCode();
		poCode.setId(PO_CODE);
		poCode.setContractType(valueTablesRepository.findContractTypeByValue(ContractTypeCode.VAT));
		poCode.setSupplier(supplier);
		po.setPoCode(poCode);
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
		ReceiptItem[] receiptItems = new ReceiptItem[NUM_PO_ITEMS];
		List<Warehouse> storages = valueTablesReader.getAllStorages();
		for(int i=0; i < receiptItems.length; i++) {
			receiptItems[i] = new ReceiptItem();
			receiptItems[i].setItem(orderItems[i].getItem());
			receiptItems[i].setOrderItem(orderItems[i]);
			
			Storage[] storageForms = new Storage[2];
			storageForms[0] = new Storage();
			storageForms[0].setUnitAmount(BigDecimal.valueOf(50));
			storageForms[0].setNumberUnits(BigDecimal.valueOf(326));
			storageForms[0].setStorageLocation(storages.get(i));
			storageForms[0].setMeasureUnit("KG");
			
			storageForms[1] = new Storage();
			storageForms[1].setUnitAmount(BigDecimal.valueOf(26));
			storageForms[1].setNumberUnits(BigDecimal.valueOf(1));
			storageForms[1].setStorageLocation(storages.get(i));
			storageForms[1].setMeasureUnit("KG");
			
			receiptItems[i].setStorageForms(storageForms);
		}
		receipt.setReceiptItems(receiptItems);
		receipts.addCashewOrderReceipt(receipt);
		ReceiptDTO receiptDTO;
		receiptDTO = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(new ReceiptDTO(receipt), receiptDTO, "Order Receipt not added or fetched correctly");
		
		//add QC for received order
		QualityCheck check = new QualityCheck();
		check.setPoCode(poCode);
		check.setRecordedTime(OffsetDateTime.now());
		RawItemQuality[] rawItemQualities = new RawItemQuality[NUM_PO_ITEMS];
		for(int i=0; i < rawItemQualities.length; i++) {
			rawItemQualities[i] = new RawItemQuality();
			rawItemQualities[i].setItem(orderItems[i].getItem());
			
			Storage[] storageForms = new Storage[1];
			storageForms[0] = new Storage();
			storageForms[0].setUnitAmount(BigDecimal.valueOf(8));
			storageForms[0].setNumberUnits(BigDecimal.valueOf(2));
			storageForms[0].setStorageLocation(storages.get(i));
			storageForms[0].setMeasureUnit("OZ");
			
		}
		check.setQualityChecks(rawItemQualities);
		checks.addCashewReceiptCheck(check);
		QualityCheckDTO checkDTO = checks.getQcByProcessId(check.getId());
		assertEquals(new QualityCheckDTO(check), checkDTO, "QC not added or fetched correctly");
		
		//print all
		System.out.println("Supplier: " + supplierDTO);
		System.out.println("Purchase Order: " + poDTO);
		System.out.println("Order receipt: " + receiptDTO);
		System.out.println("QC test: " + checkDTO);
		
		//print received orders
		List<ReceiptRow> receiptRows = receipts.findCashewReceipts();
		receiptRows.forEach(r -> System.out.println(r));
		
		//remove all
		checks.removeCheck(check.getId());
		receipts.removeReceipt(receiptDTO.getId());
		orders.removeOrder(poDTO.getId());
		suppliers.permenentlyRemoveEntity(poCode);
		suppliers.permenentlyRemoveSupplier(supplierDTO.getId());
		
		
	}
}
