/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.data.DataObject;
import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.SampleReceiptDTO;
import com.avc.mis.beta.dto.process.collection.CashewItemQualityDTO;
import com.avc.mis.beta.dto.process.collection.OrderItemDTO;
import com.avc.mis.beta.dto.process.collection.ProcessFileDTO;
import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.dto.process.collection.ReceiptItemDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageWithSampleDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemWithMeasureUnit;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.data.ProcessFile;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.SampleReceipt;
import com.avc.mis.beta.entities.process.collection.CashewItemQuality;
import com.avc.mis.beta.entities.process.collection.ItemWeight;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.collection.ReceiptItem;
import com.avc.mis.beta.entities.process.collection.SampleItem;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageWithSample;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.repositories.ValueTablesRepository;
import com.avc.mis.beta.service.ObjectWriter;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Samples;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.ValueWriter;

import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class GeneralTest {
	
	static final Integer PO_CODE = 800241;
	static final Integer NUM_PO_ITEMS = 2;
	static final Integer NUM_OF_CHECKS = 1;
	
	@Autowired ValueTablesRepository valueTablesRepository;
	
	@Autowired ValueTablesReader valueTablesReader;
	@Autowired Suppliers suppliers;
	@Autowired Orders orders;
	@Autowired Receipts receipts;
	@Autowired QualityChecks checks;
	@Autowired Samples samples;
	
	@Autowired ObjectWriter objectWriter;
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
		PoDTO po = new PoDTO();
		PoCode poCode = new PoCode();
		poCode.setCode(Integer.toString(PO_CODE));
		poCode.setContractType(valueTablesRepository.findContractTypeByCodeAndCurrency("VAT", Currency.getInstance("VND")));
		poCode.setSupplier(supplier);
		objectWriter.addPoCode(poCode);
		po.setPoCode(new PoCodeBasic(poCode));
		po.setRecordedTime(LocalDateTime.now());
		List<OrderItemDTO> orderItems = new ArrayList<OrderItemDTO>();
		List<Item> items = valueTablesReader.getAllItems();
		for(int i=0; i < NUM_PO_ITEMS; i++) {
			OrderItemDTO orderItem = new OrderItemDTO();
			orderItems.add(orderItem);
			orderItem.setItem(new ItemWithMeasureUnit(items.get(i)));
			orderItem.setNumberUnits(new AmountWithUnit("35000", "LBS"));
//			orderItem.setCurrency("USD");
//			orderItem.setMeasureUnit("LBS");
			orderItem.setUnitPrice(new AmountWithCurrency("2.99", "USD"));
			orderItem.setDeliveryDate(LocalDate.now().toString());			
		}
		po.setOrderItems(orderItems);
		Integer poId = orders.addCashewOrder(po);
		PoDTO poDTO = orders.getOrderByProcessId(poId);
		assertEquals(po, poDTO, "PO not added or fetched correctly");
		
		//change order process life cycle to lock process for editing
		processInfoWriter.setEditStatus(EditStatus.LOCKED, poId);
		poDTO = orders.getOrderByProcessId(poId);
		assertEquals(EditStatus.LOCKED, poDTO.getEditStatus(), "Didn't change life cycle record edit status");
		try {
			processInfoWriter.setProcessStatus(ProcessStatus.PENDING, poId);
			fail("Should not be able to change to previous life cycle status");
		} catch (Exception e1) {}
		//check that process can't be edited after it's locked
		po.setDowntime(Duration.ofHours(24));
		try {
			orders.editOrder(po);
			fail("Should not be able to edit at locked status life cycle");
		} catch (Exception e1) {}
		
		//receive both order lines in parts and different storages
		ReceiptDTO receipt = new ReceiptDTO();
		receipt.setPoCode(new PoCodeBasic(poCode));
		receipt.setRecordedTime(LocalDateTime.now());
		List<ReceiptItemDTO> receiptItems = new ArrayList<>();
		List<Warehouse> storages = valueTablesReader.getAllWarehouses();
		List<OrderItemDTO> fetchedOrderItems = poDTO.getOrderItems();
		for(int i=0; i < NUM_PO_ITEMS; i++) {
			ReceiptItemDTO receiptItem = new ReceiptItemDTO();
			ItemWithMeasureUnit item = fetchedOrderItems.get(i).getItem();
			receiptItem.setItem(new ItemWithUnitDTO((Item)item.fillEntity(new Item())));
			receiptItem.setMeasureUnit(item.getMeasureUnit());
			receiptItem.setReceivedOrderUnits(new AmountWithUnit(BigDecimal.valueOf(35000), item.getMeasureUnit()));
			receiptItem.setUnitPrice(new AmountWithCurrency("2.99", "USD"));
			receiptItem.setOrderItem(new DataObject<OrderItem>(fetchedOrderItems.get(i).getId(), fetchedOrderItems.get(i).getVersion()));
			
			StorageWithSampleDTO[] storageForms = new StorageWithSampleDTO[2];
			StorageWithSampleDTO storage = new StorageWithSampleDTO();
			storage.setUnitAmount(BigDecimal.valueOf(50));
			storage.setNumberUnits(BigDecimal.valueOf(326));
			storage.setWarehouseLocation(new BasicValueEntity<Warehouse>(storages.get(i)));
//			storage.setMeasureUnit("KG");
			storageForms[0] = storage;
			
			storage = new StorageWithSampleDTO();
			storage.setUnitAmount(BigDecimal.valueOf(26));
			storage.setNumberUnits(BigDecimal.valueOf(1));
			storage.setWarehouseLocation(new BasicValueEntity<Warehouse>(storages.get(i)));
//			storage.setMeasureUnit("KG");
			storageForms[1] = storage;
			
			receiptItem.setStorageForms(storageForms);
			receiptItems.add(receiptItem);
		}
		receipt.setReceiptItems(receiptItems);
		try {
			receipt.setId(receipts.addCashewOrderReceipt(receipt));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		ReceiptDTO receiptDTO;
		receiptDTO = receipts.getReceiptByProcessId(receipt.getId());
		assertEquals(receipt, receiptDTO, "Order Receipt not added or fetched correctly");
		
		
		//add QC for received order
		QualityCheckDTO check = new QualityCheckDTO();
		check.setPoCode(new PoCodeBasic(poCode));
		check.setRecordedTime(LocalDateTime.now());
		check.setCheckedBy("avc lab");
		List<CashewItemQualityDTO> rawItemQualities = new ArrayList<CashewItemQualityDTO>();
		List<ProcessItemDTO> processItems = new ArrayList<ProcessItemDTO>();
		for(int i=0; i < NUM_PO_ITEMS; i++) {
			CashewItemQualityDTO rawItemQuality = new CashewItemQualityDTO();
			rawItemQualities.add(rawItemQuality);
			ItemWithMeasureUnit item = orderItems.get(i).getItem();
			rawItemQuality.setItem(new BasicValueEntity<Item>(item.getId(), item.getValue()));
			rawItemQuality.setMeasureUnit(MeasureUnit.OZ);
			rawItemQuality.setSampleWeight(BigDecimal.valueOf(8).setScale(QualityCheck.SCALE));
			rawItemQuality.setNumberOfSamples(BigInteger.TEN);
			rawItemQuality.setDefects(new RawDefects());
			rawItemQuality.setDamage(new RawDamage());
			
			ProcessItemDTO processItem = new ProcessItemDTO();
			processItems.add(processItem);
			ItemWithUnitDTO itemWithUnitDTO = new ItemWithUnitDTO(orderItems.get(i).getItem().fillEntity(new Item()));
			processItem.setItem(itemWithUnitDTO);
			processItem.setMeasureUnit(item.getMeasureUnit());
			
			List<StorageDTO> QCStorageForms = new ArrayList<StorageDTO>();
			StorageDTO QCStorageForm = new StorageDTO();
			QCStorageForms.add(QCStorageForm);
			QCStorageForm.setUnitAmount(BigDecimal.valueOf(8));
			QCStorageForm.setNumberUnits(BigDecimal.valueOf(2));
			QCStorageForm.setWarehouseLocation(new BasicValueEntity<Warehouse>(storages.get(i)));
//			QCStorageForm.setMeasureUnit("OZ");
			
			processItem.setStorageForms(QCStorageForms);
			
		}
		check.setProcessItems(processItems);
		check.setTestedItems(rawItemQualities);
		Integer checkId;
		try {
			checkId = checks.addCashewReceiptCheck(check);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
		QualityCheckDTO checkDTO;
		checkDTO = checks.getQcByProcessId(checkId);
		assertEquals(check, checkDTO, "QC not added or fetched correctly");

		ProcessFileDTO processFile = new ProcessFileDTO(null, null, checkId, "address", 
				"description", "remarks", null, null);
		objectWriter.addProcessFile(processFile);
		try {
			
			check.setProcessFiles(Arrays.asList(processFile));
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
		checkDTO = checks.getQcByProcessId(checkId);
//		System.out.println(checkDTO);
//		fail("finished");
		try {
			assertEquals(check, checkDTO, "QC not added or fetched correctly");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}

		//add receipt sample check for received orders
		SampleReceipt sampleReceipt = new SampleReceipt();
		sampleReceipt.setPoCode(poCode);
		sampleReceipt.setRecordedTime(LocalDateTime.now());
		SampleItem[] sampleItems = new SampleItem[2];
		sampleItems[0] = new SampleItem();
		sampleItems[0].setItem(items.get(0));
		sampleItems[0].setMeasureUnit(MeasureUnit.KG);
//		sampleItems[0].setMeasureUnit("KG");
		ItemWeight[] itemWeights1 = new ItemWeight[1];
		itemWeights1[0] = new ItemWeight();
		itemWeights1[0].setUnitAmount(BigDecimal.valueOf(50));
		itemWeights1[0].setNumberUnits(BigDecimal.TEN);
		itemWeights1[0].setNumberOfSamples(BigInteger.valueOf(30));
		itemWeights1[0].setAvgTestedWeight(BigDecimal.valueOf(50.01));
		sampleItems[0].setItemWeights(itemWeights1);
		sampleItems[0].setSampleContainerWeight(BigDecimal.valueOf(0.002));
		sampleItems[1] = new SampleItem();
		sampleItems[1].setItem(items.get(0));
		sampleItems[1].setMeasureUnit(MeasureUnit.KG);
//		sampleItems[1].setMeasureUnit("KG");
		ItemWeight[] itemWeights2 = new ItemWeight[1];
		itemWeights2[0] = new ItemWeight();
		itemWeights2[0].setUnitAmount(BigDecimal.valueOf(26));
		itemWeights2[0].setNumberUnits(BigDecimal.TEN);
		itemWeights2[0].setNumberOfSamples(BigInteger.valueOf(1));
		itemWeights2[0].setAvgTestedWeight(BigDecimal.valueOf(26.01));
		sampleItems[1].setItemWeights(itemWeights2);
		sampleItems[1].setSampleContainerWeight(BigDecimal.valueOf(0.002));
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
//		samples.removeSampleReceipt(sampleReceipt.getId());
//		checks.removeCheck(check.getId());
//		receipts.removeReceipt(receiptDTO.getId());
//		orders.removeOrder(poDTO.getId());
//		suppliers.permenentlyRemoveEntity(poCode);
//		suppliers.permenentlyRemoveSupplier(supplierDTO.getId());
		
		processInfoWriter.removeAllProcesses(poCode.getId());
		suppliers.permenentlyRemoveSupplier(supplierDTO.getId());

	}
}
