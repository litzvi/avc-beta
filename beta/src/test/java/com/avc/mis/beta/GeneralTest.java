/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Currency;
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
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.SampleReceiptDTO;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.SampleReceipt;
import com.avc.mis.beta.entities.processinfo.ItemWeight;
import com.avc.mis.beta.entities.processinfo.OrderItem;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.RawItemQuality;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.entities.processinfo.SampleItem;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.repositories.ValueTablesRepository;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Samples;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.ValueWriter;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class GeneralTest {
	
	static final Integer PO_CODE = 800098;
	static final Integer NUM_PO_ITEMS = 2;
	static final Integer NUM_OF_CHECKS = 1;
	
	@Autowired ValueTablesRepository valueTablesRepository;
	
	@Autowired ValueTablesReader valueTablesReader;
	@Autowired Suppliers suppliers;
	@Autowired Orders orders;
	@Autowired Receipts receipts;
	@Autowired QualityChecks checks;
	@Autowired Samples samples;
	
	@Autowired ValueWriter valueWriter;
	@Autowired ProcessInfoWriter processInfoWriter;
	
	@Test
	void orderAndReceiveTest() {
		//create basic supplier with all existing supply categories
		Supplier supplier = new Supplier();
		supplier.setName("Test supplier" + PO_CODE);
		supplier.setSupplyCategories(valueTablesReader.getAllSupplyCategories().stream().collect(Collectors.toSet()));
		suppliers.addSupplier(supplier);
		SupplierDTO supplierDTO = suppliers.getSupplier(supplier.getId());
		assertEquals(new SupplierDTO(supplier, true), supplierDTO, "Supplier not added or fetched correctly");
		
		//create a cashew order with 2 order lines
		PO po = new PO();
		PoCode poCode = new PoCode();
		poCode.setId(PO_CODE);
		poCode.setContractType(valueTablesRepository.findContractTypeByCodeAndCurrency("VAT", Currency.getInstance("VND")));
		poCode.setSupplier(supplier);
		po.setPoCode(poCode);
		po.setRecordedTime(OffsetDateTime.now());
		OrderItem[] orderItems = new OrderItem[NUM_PO_ITEMS];
		List<Item> items = valueTablesReader.getAllItems();
		for(int i=0; i < NUM_PO_ITEMS; i++) {
			orderItems[i] = new OrderItem();
			orderItems[i].setItem(items.get(i));
			orderItems[i].setNumberUnits(new AmountWithUnit("35000", "LBS"));
//			orderItems[i].setCurrency("USD");
//			orderItems[i].setMeasureUnit("LBS");
			orderItems[i].setUnitPrice(new AmountWithCurrency("2.99", "USD"));
			orderItems[i].setDeliveryDate(LocalDate.now().toString());			
		}
		po.setOrderItems(orderItems);
		orders.addCashewOrder(po);
		PoDTO poDTO = orders.getOrderByProcessId(po.getId());
		assertEquals(new PoDTO(po), poDTO, "PO not added or fetched correctly");
		
		//change order process life cycle to lock process for editing
		processInfoWriter.setEditStatus(EditStatus.LOCKED, po.getId());
		poDTO = orders.getOrderByProcessId(po.getId());
		assertEquals(EditStatus.LOCKED, poDTO.getEditStatus(), "Didn't change life cycle record edit status");
		try {
			processInfoWriter.setProcessStatus(ProcessStatus.PENDING, po.getId());
			fail("Should not be able to change to previous life cycle status");
		} catch (Exception e1) {}
		//check that process can't be edited after it's locked
		po.setDuration(Duration.ofHours(24));
		try {
			orders.editOrder(po);
			fail("Should not be able to edit at locked status life cycle");
		} catch (Exception e1) {}
		
		//receive both order lines in parts and different storages
		Receipt receipt = new Receipt();
		receipt.setPoCode(poCode);
		receipt.setRecordedTime(OffsetDateTime.now());
		ReceiptItem[] receiptItems = new ReceiptItem[NUM_PO_ITEMS];
		List<Warehouse> storages = valueTablesReader.getAllWarehouses();
		for(int i=0; i < receiptItems.length; i++) {
			receiptItems[i] = new ReceiptItem();
			receiptItems[i].setItem(orderItems[i].getItem());
			receiptItems[i].setOrderItem(orderItems[i]);
			
			Storage[] storageForms = new Storage[2];
			storageForms[0] = new Storage();
			storageForms[0].setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(50), "KG"));
			storageForms[0].setNumberUnits(BigDecimal.valueOf(326));
			storageForms[0].setWarehouseLocation(storages.get(i));
//			storageForms[0].setMeasureUnit("KG");
			
			storageForms[1] = new Storage();
			storageForms[1].setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(26), "KG"));
			storageForms[1].setNumberUnits(BigDecimal.valueOf(1));
			storageForms[1].setWarehouseLocation(storages.get(i));
//			storageForms[1].setMeasureUnit("KG");
			
			receiptItems[i].setStorageForms(storageForms);
		}
		receipt.setReceiptItems(receiptItems);
		try {
			receipts.addCashewOrderReceipt(receipt);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
		ReceiptDTO receiptDTO;
		receiptDTO = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(new ReceiptDTO(receipt), receiptDTO, "Order Receipt not added or fetched correctly");
		
		
		//add QC for received order
		QualityCheck check = new QualityCheck();
		check.setPoCode(poCode);
		check.setRecordedTime(OffsetDateTime.now());
		RawItemQuality[] rawItemQualities = new RawItemQuality[NUM_PO_ITEMS];
		ProcessItem[] processItems = new ProcessItem[NUM_PO_ITEMS];
		for(int i=0; i < NUM_PO_ITEMS; i++) {
			rawItemQualities[i] = new RawItemQuality();
			rawItemQualities[i].setItem(orderItems[i].getItem());
			rawItemQualities[i].setMeasureUnit(MeasureUnit.OZ);
			rawItemQualities[i].setSampleWeight(BigDecimal.valueOf(8).setScale(MeasureUnit.SCALE));
			
			
			processItems[i] = new ProcessItem();
			processItems[i].setItem(orderItems[i].getItem());
			
			Storage[] QCStorageForms = new Storage[1];
			QCStorageForms[0] = new Storage();
			QCStorageForms[0].setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(8), "OZ"));
			QCStorageForms[0].setNumberUnits(BigDecimal.valueOf(2));
			QCStorageForms[0].setWarehouseLocation(storages.get(i));
//			QCStorageForms[0].setMeasureUnit("OZ");
			
			processItems[i].setStorageForms(QCStorageForms);
			
		}
		check.setProcessItems(processItems);
		check.setTestedItems(rawItemQualities);
		try {
			checks.addCashewReceiptCheck(check);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		QualityCheckDTO checkDTO;
		checkDTO = checks.getQcByProcessId(check.getId());
//		fail("finished");
		assertEquals(new QualityCheckDTO(check), checkDTO, "QC not added or fetched correctly");

		//add receipt sample check for received orders
		SampleReceipt sampleReceipt = new SampleReceipt();
		sampleReceipt.setPoCode(poCode);
		sampleReceipt.setRecordedTime(OffsetDateTime.now());
		SampleItem[] sampleItems = new SampleItem[2];
		sampleItems[0] = new SampleItem();
		sampleItems[0].setItem(items.get(0));
		sampleItems[0].setMeasureUnit(MeasureUnit.KG);
//		sampleItems[0].setMeasureUnit("KG");
		ItemWeight[] itemWeights1 = new ItemWeight[1];
		itemWeights1[0] = new ItemWeight();
		itemWeights1[0].setUnitAmount(BigDecimal.valueOf(50));
		itemWeights1[0].setNumberOfSamples(BigInteger.valueOf(30));
		itemWeights1[0].setAvgTestedWeight(BigDecimal.valueOf(50.01));
		sampleItems[0].setItemWeights(itemWeights1);
		sampleItems[0].setEmptyContainerWeight(BigDecimal.valueOf(0.002));
		sampleItems[1] = new SampleItem();
		sampleItems[1].setItem(items.get(0));
		sampleItems[1].setMeasureUnit(MeasureUnit.KG);
//		sampleItems[1].setMeasureUnit("KG");
		ItemWeight[] itemWeights2 = new ItemWeight[1];
		itemWeights2[0] = new ItemWeight();
		itemWeights2[0].setUnitAmount(BigDecimal.valueOf(26));
		itemWeights2[0].setNumberOfSamples(BigInteger.valueOf(1));
		itemWeights2[0].setAvgTestedWeight(BigDecimal.valueOf(26.01));
		sampleItems[1].setItemWeights(itemWeights2);
		sampleItems[1].setEmptyContainerWeight(BigDecimal.valueOf(0.002));
		sampleReceipt.setSampleItems(sampleItems);
		try {
			samples.addSampleReceipt(sampleReceipt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		SampleReceiptDTO sampleReceiptDTO;
		System.out.println("line 202");
		sampleReceiptDTO = samples.getSampleReceiptByProcessId(sampleReceipt.getId());
		assertEquals(new SampleReceiptDTO(sampleReceipt), sampleReceiptDTO, "Receipt sample not added or fetched correctly");
		
		
		
		//print all
		System.out.println("Supplier: " + supplierDTO);
		System.out.println("Purchase Order: " + poDTO);
		System.out.println("Order receipt: " + receiptDTO);
		System.out.println("QC test: " + checkDTO);
		System.out.println("Receipt sample: " + sampleReceiptDTO);
		
		
		//remove all
		samples.removeSampleReceipt(sampleReceipt.getId());
		checks.removeCheck(check.getId());
		receipts.removeReceipt(receiptDTO.getId());
		orders.removeOrder(poDTO.getId());
		suppliers.permenentlyRemoveEntity(poCode);
		suppliers.permenentlyRemoveSupplier(supplierDTO.getId());
		
		
		
	}
}
