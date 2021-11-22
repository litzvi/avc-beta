/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.basic.DataObjectWithName;
import com.avc.mis.beta.dto.basic.ItemWithMeasureUnit;
import com.avc.mis.beta.dto.basic.ItemWithUnitDTO;
import com.avc.mis.beta.dto.basic.ItemWithUse;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.PoCodeBasicWithProductCompany;
import com.avc.mis.beta.dto.codes.GeneralPoCodeDTO;
import com.avc.mis.beta.dto.codes.PoCodeDTO;
import com.avc.mis.beta.dto.codes.ProductPoCodeDTO;
import com.avc.mis.beta.dto.codes.ShipmentCodeDTO;
import com.avc.mis.beta.dto.data.DataObject;
import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.collectionItems.CountAmountDTO;
import com.avc.mis.beta.dto.process.collectionItems.OrderItemDTO;
import com.avc.mis.beta.dto.process.group.ItemCountDTO;
import com.avc.mis.beta.dto.process.group.ProcessItemDTO;
import com.avc.mis.beta.dto.process.group.ReceiptItemDTO;
import com.avc.mis.beta.dto.process.group.StorageMovesGroupDTO;
import com.avc.mis.beta.dto.process.group.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.process.storages.ExtraAddedDTO;
import com.avc.mis.beta.dto.process.storages.StorageBaseDTO;
import com.avc.mis.beta.dto.process.storages.StorageDTO;
import com.avc.mis.beta.dto.process.storages.StorageMoveDTO;
import com.avc.mis.beta.dto.process.storages.StorageWithSampleDTO;
import com.avc.mis.beta.dto.process.storages.UsedItemDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.ContractTypeDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ProductionLineDTO;
import com.avc.mis.beta.dto.values.ShippingPortDTO;
import com.avc.mis.beta.dto.values.SupplyCategoryDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.collectionItems.OrderItem;
import com.avc.mis.beta.entities.system.WeightedPo;
import com.avc.mis.beta.entities.values.CashewItem;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.service.Loading;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ObjectWriter;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;

/**
 * @author Zvi
 *
 */
@Service
public class TestService {
	@Autowired ProcessInfoWriter processInfoWriter;
	
	@Autowired private Suppliers suppliers;	
	@Autowired ValueTablesReader valueTableReader;
	@Autowired ObjectWriter objectWriter;
	@Autowired ObjectTablesReader objectTablesReader;
	@Autowired Orders orders;
	@Autowired Receipts receipts;
	@Autowired Loading loadings;
	
	private int randCode = LocalDateTime.now().hashCode();
	private Random randNum = new Random();
	
	public SupplierDTO addBasicSupplier() {
		SupplierDTO supplier = new SupplierDTO();
		supplier.setName("service supplier " + randCode++);
		Integer supplierId = suppliers.addSupplier(supplier);
		return suppliers.getSupplier(supplierId);
	}
		
	public ShipingDetails getShipingDetails() {
		ShipingDetails shipingDetails = new ShipingDetails();
		shipingDetails.setEtd("2007-12-03");
		shipingDetails.setEta("2008-12-03");
		return shipingDetails;
	}

	PoCodeDTO addPoCode() {
		ProductPoCodeDTO poCode = new ProductPoCodeDTO();
		poCode.setCode(Integer.toString(randCode++));
		SupplierDTO supplier = addBasicSupplier();
		poCode.setSupplier(new DataObjectWithName<Supplier>(supplier.getId(), supplier.getVersion(), supplier.getName()));
		poCode.setContractType(getContractType());
		Integer poCodeId = objectWriter.addPoCode(poCode);
		return objectWriter.getPoCode(poCodeId);
	}
	
	PoCodeDTO addGeneralPoCode() {
		GeneralPoCodeDTO poCode = new GeneralPoCodeDTO();
		poCode.setCode(Integer.toString(randCode++));
		SupplierDTO supplier = addBasicSupplier();
		poCode.setCode(Integer.toString(randCode++));
		poCode.setSupplier(new DataObjectWithName<Supplier>(supplier.getId(), supplier.getVersion(), supplier.getName()));
		poCode.setContractType(getContractType());
		Integer poCodeId = objectWriter.addPoCode(poCode);
		return objectWriter.getPoCode(poCodeId);
	}

	public ShipmentCodeDTO addShipmentCode() {
		ShipmentCodeDTO shipmentCode = new ShipmentCodeDTO();
		shipmentCode.setCode(Integer.toString(randCode++));
		shipmentCode.setPortOfDischarge(getShippingPort());
		Integer id = objectWriter.addShipmentCode(shipmentCode);
		shipmentCode = objectWriter.getShipmentCode(id);
		return shipmentCode;
	}

	
	public PoDTO addBasicCashewOrder() {
		
		//build purchase order
		PoDTO po = new PoDTO();
		PoCodeDTO poCode = addPoCode();
		po.setPoCode(new PoCodeBasic(poCode));
//		poCode.setCode(Integer.toString(randCode++));
//		Supplier supplier = addBasicSupplier();
//		poCode.setSupplier(supplier);
//		poCode.setContractType(getContractType());
		
		//build process
		po.setRecordedTime(LocalDateTime.now());
		
		//add order items
		List<OrderItemDTO> items = getOrderItems(OrdersTest.NUM_ITEMS, ItemGroup.PRODUCT);				
		po.setOrderItems(items);
		Integer poId = orders.addCashewOrder(po);
		po.setId(poId);
		return po;
	}
	
	public PoDTO addBasicGeneralOrder() {
		
		//build purchase order
		PoDTO po = new PoDTO();
		PoCodeDTO poCode = addGeneralPoCode();
//		Supplier supplier = addBasicSupplier();
//		poCode.setCode(Integer.toString(randCode++));
//		poCode.setSupplier(supplier);
//		poCode.setContractType(getContractType());
		po.setPoCode(new PoCodeBasic(poCode));
		
		//build process
		po.setRecordedTime(LocalDateTime.now());
		
		//add order items
		List<OrderItemDTO> items = getOrderItems(OrdersTest.NUM_ITEMS, ItemGroup.GENERAL);				
		po.setOrderItems(items);
		Integer poId = orders.addGeneralOrder(po);
		po.setId(poId);
		return po;
	}
	
	private List<OrderItemDTO> getOrderItems(int numOfItems, ItemGroup group) {
		List<OrderItemDTO> orderItems = new ArrayList<OrderItemDTO>();
		for(int i=0; i<numOfItems; i++) {
			OrderItemDTO orderItem = new OrderItemDTO();
			orderItems.add(orderItem);
			Item item = getItemByGroup(group);
			ItemWithMeasureUnit itemWithMeasureUnit = new ItemWithMeasureUnit(item.getId(), item.getValue(), item.getMeasureUnit());
			orderItem.setItem(itemWithMeasureUnit);
			orderItem.setNumberUnits(new AmountWithUnit(new BigDecimal(i+1), item.getMeasureUnit()));
			orderItem.setUnitPrice(new AmountWithCurrency("1.16", "USD"));
			orderItem.setDeliveryDate("1983-11-23");
		}
		return orderItems;
	}
	
	public ReceiptDTO addOneItemCashewReceipt() {
		//build order receipt
		ReceiptDTO receipt = new ReceiptDTO();
		PoCodeBasic poCode = new PoCodeBasic(addPoCode());
//		poCode.setCode(Integer.toString(randCode++));
//		Supplier supplier = addBasicSupplier();
//		poCode.setSupplier(supplier);
//		poCode.setContractType(getContractType());
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(LocalDateTime.now());
		//add order items
		receipt.setReceiptItems(getReceiptItems(1));
		receipt.setId(receipts.addCashewReceipt(receipt));
		return receipt;
	}
	
	public ReceiptDTO addBasicCashewReceipt() {
		//build order receipt
		ReceiptDTO receipt = new ReceiptDTO();
		PoCodeBasic poCode = new PoCodeBasic(addPoCode());
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(LocalDateTime.now());
		receipt.setStartTime(LocalTime.parse("20:15"));
		//add order items
		receipt.setReceiptItems(getReceiptItems(OrdersTest.NUM_ITEMS));
		receipt.setId(receipts.addCashewReceipt(receipt));
		return receipt;
	}

 List<ReceiptItemDTO> getReceiptItems(int numOfItems) {
		List<ReceiptItemDTO> receiptItems = new ArrayList<>();
//		List<StorageDTO> storageForms = new ArrayList<StorageDTO>();
		List<ExtraAddedDTO> added = new ArrayList<>();
		BasicValueEntity<Warehouse> warehouse = getWarehouse();
		for(int i=0; i<numOfItems; i++) {
			StorageWithSampleDTO storageForm = new StorageWithSampleDTO();
//			storageForms.add(storageForm);
			storageForm.setUnitAmount(BigDecimal.ONE);
//			storageForm.setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(1), "LBS"));
			storageForm.setNumberUnits(BigDecimal.valueOf(35000));
			storageForm.setWarehouseLocation(warehouse);
//			storageForm.setSampleContainerWeight(BigDecimal.valueOf(0.002));
			storageForm.setNumberOfSamples(BigInteger.valueOf(30));
			storageForm.setAvgTestedWeight(BigDecimal.valueOf(50.01));
			//build receipt item
			ReceiptItemDTO receiptItem = new ReceiptItemDTO();
			receiptItems.add(receiptItem);
			Item item = getItem();
			receiptItem.setItem(new ItemWithUnitDTO(item));
			receiptItem.setReceivedOrderUnits(new AmountWithUnit(BigDecimal.valueOf(35000), item.getMeasureUnit()));
			receiptItem.setMeasureUnit(item.getMeasureUnit());
			receiptItem.setUnitPrice(new AmountWithCurrency("2.99", "USD"));
			receiptItem.setStorageForms(new StorageWithSampleDTO[] {storageForm});
			//add extra bonus
			ExtraAddedDTO add = new ExtraAddedDTO();
			added.add(add);
			add.setUnitAmount(BigDecimal.ONE);//because database is set to scale 2
			add.setNumberUnits(new BigDecimal(4).setScale(2));
			receiptItem.setExtraAdded(Arrays.asList(add));
		}
		return receiptItems;
	}
	
	public ReceiptDTO getCashewOrderReceipt(int orderPoCode) {
		//build order receipt
		ReceiptDTO receipt = new ReceiptDTO();
		PoCodeBasic poCode = new PoCodeBasic();
		poCode.setId(orderPoCode);
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(LocalDateTime.now());
		//add order items
		PoDTO poDTO = orders.getOrder(orderPoCode);
		receipt.setReceiptItems(getOrderReceiptItems(poDTO));
		receipt.setId(receipts.addCashewOrderReceipt(receipt));
		return receipt;		
	}
	
	public ReceiptDTO getGeneralOrderReceipt(int orderPoCode) {
		//build order receipt
		ReceiptDTO receipt = new ReceiptDTO();
		PoCodeBasic poCode = new PoCodeBasic();
		poCode.setId(orderPoCode);
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(LocalDateTime.now());
		//add order items
		PoDTO poDTO = orders.getOrder(orderPoCode);
		receipt.setReceiptItems(getOrderReceiptItems(poDTO));
		receipt.setId(receipts.addGeneralOrderReceipt(receipt));
		return receipt;		
	}
	
	private List<ReceiptItemDTO> getOrderReceiptItems(PoDTO poDTO) {
		List<OrderItemDTO> orderItems = poDTO.getOrderItems();

		List<ReceiptItemDTO> receiptItems = new ArrayList<>();
//		List<StorageDTO> storageForms = new ArrayList<StorageDTO>();
		BasicValueEntity<Warehouse>warehouse = getWarehouse();
		DataObject<OrderItem> oi;
		int i=0;
		for(OrderItemDTO oItem: orderItems) {
			ReceiptItemDTO receiptItem = new ReceiptItemDTO();
			receiptItems.add(receiptItem);
			StorageWithSampleDTO storageForm = new StorageWithSampleDTO();
//			storageForms.add(storageForm);
			int itemId = oItem.getItem().getId();
			ItemDTO itemDTO = getItemsByGroup(null).stream().filter(j -> j.getId() == itemId).findAny().get();
			ItemWithUnitDTO item = new ItemWithUnitDTO(itemDTO.getId());
			item.setMeasureUnit(itemDTO.getMeasureUnit());
			receiptItem.setItem(item);
			receiptItem.setReceivedOrderUnits(new AmountWithUnit(BigDecimal.valueOf(35000), item.getMeasureUnit()));
			receiptItem.setMeasureUnit(item.getMeasureUnit());
			receiptItem.setUnitPrice(new AmountWithCurrency("2.99", "USD"));
			storageForm.setUnitAmount(BigDecimal.ONE);
			storageForm.setNumberUnits(BigDecimal.valueOf(35000));
			storageForm.setWarehouseLocation(warehouse);
//			storageForm.setSampleContainerWeight(BigDecimal.valueOf(0.002));
			storageForm.setNumberOfSamples(BigInteger.valueOf(30));
			storageForm.setAvgTestedWeight(BigDecimal.valueOf(50.01));
			receiptItem.setStorageForms(new StorageWithSampleDTO[] {storageForm});
			oi  = new DataObject<OrderItem>(oItem.getId(), oItem.getVersion());
			receiptItem.setOrderItem(oi);
			receiptItem.setExtraRequested(new AmountWithUnit(BigDecimal.valueOf(200), "KG"));
			i++;
		}
		return receiptItems;
	}
	
	public BasicValueEntity<Warehouse> getWarehouse() {
		List<BasicValueEntity<Warehouse>> warehouses = valueTableReader.getAllWarehousesBasic();
		if(warehouses.isEmpty())
			fail("No warehouses in database for running this test");
		return warehouses.get(randNum.nextInt(warehouses.size()));
	}
	
	public PoCodeBasic getPoCodeBasic() {
		List<PoCodeBasicWithProductCompany> poCodes = objectTablesReader.findAllProductPoCodes();
		if(poCodes.isEmpty())
			fail("No po codes in database for running this test");
		return poCodes.get(randNum.nextInt(poCodes.size()));
	}	

	public List<ItemDTO> getItemsByGroup(ItemGroup group) {
		List<ItemDTO> items = valueTableReader.getItemsByGroup(group);
		if(items.isEmpty())
			fail("No items in database for running this test");
		return items;
		
	}
	
	public Item getItem() {
		return getItemByGroup(null);
	}
	
	public Item getItemByGroup(ItemGroup group) {
		List<ItemDTO> items = getItemsByGroup(group);
		ItemDTO item = items.get(randNum.nextInt(items.size()));
		
		return getItem(item);
	}
	
	public ContractTypeDTO getContractType() {
		List<BasicValueEntity<ContractType>> contractTypes = valueTableReader.getAllContractTypesBasic();
		if(contractTypes.isEmpty())
			fail("No Contract Types in database for running this test");
		ContractTypeDTO contractTypeDTO = new ContractTypeDTO();
		contractTypeDTO.setId(contractTypes.get(randNum.nextInt(contractTypes.size())).getId());
//		return contractTypes.get(randNum.nextInt(contractTypes.size()));
		return contractTypeDTO;
	}
	
	public CityDTO getCity() {
		List<CityDTO> cities = valueTableReader.getAllCities();
		if(cities.isEmpty())
			fail("No Cities in database");
		return cities.get(randNum.nextInt(cities.size()));
	}
	
	public BankBranchDTO getBankBranch() {
		List<BankBranchDTO> branches = valueTableReader.getAllBankBranches();
		if(branches.isEmpty())
			fail("No Bank Branches in database");
		return branches.get(randNum.nextInt(branches.size()));
	}
	
	public List<SupplyCategoryDTO> getSupplyCategories() {
		List<SupplyCategoryDTO> supplyCategories = valueTableReader.getAllSupplyCategories();
		if(supplyCategories.isEmpty())
			fail("No Supply Categories in database");
		return supplyCategories;
	}

	public Object getSupplycategory() {
		List<SupplyCategoryDTO> supplyCategories = getSupplyCategories();
		return supplyCategories.get(randNum.nextInt(supplyCategories.size()));
	}
	
	public ShippingPortDTO getShippingPort() {
		List<ShippingPortDTO> ports = valueTableReader.getAllShippingPorts();
		if(ports.isEmpty())
			fail("No Shipping Ports in database");
		return ports.get(randNum.nextInt(ports.size()));
	}
	
	public ProductionLineDTO getProductionLine(ProductionFunctionality functionality) {
		List<ProductionLineDTO> productionLines = valueTableReader.getProductionLinesByFuncionality(new ProductionFunctionality[] {functionality});
		if(productionLines.isEmpty())
			fail("No production Lines in database");
		return productionLines.get(randNum.nextInt(productionLines.size()));
	}

	public void cleanup(Supplier supplier) {
		suppliers.permenentlyRemoveSupplier(supplier.getId());		
	}
	public void cleanupSupplier(Integer supplierId) {
		suppliers.permenentlyRemoveSupplier(supplierId);		
	}
	
	public void cleanup(PO po) {
		BasePoCode poCode = po.getPoCode();
		Supplier supplier = poCode.getSupplier();
		processInfoWriter.removeAllProcesses(poCode.getId());
//		processInfoWriter.removeProcess(po.getId());
//		orders.removeOrder(po.getId());
//		suppliers.permenentlyRemoveEntity(poCode);
		cleanup(supplier);
		
	}
	public void cleanup(PoDTO po) {
		PoCodeBasic poCode = po.getPoCode();
		PoCodeDTO poCodeDTO = objectWriter.getPoCode(poCode.getId());
		DataObjectWithName<Supplier> supplier = poCodeDTO.getSupplier();
		processInfoWriter.removeAllProcesses(poCode.getId());
		cleanupSupplier(supplier.getId());
		
	}

	public void cleanup(Receipt receipt) {
		processInfoWriter.removeProcess(receipt.getId());
//		receipts.removeReceipt(receipt.getId());
	}
	
	public void cleanup(ReceiptDTO receipt) {
		processInfoWriter.removeProcess(receipt.getId());
//		receipts.removeReceipt(receipt.getId());
	}
	
//	public static UsedItemsGroup[] getUsedItemsGroups(List<ProcessItemInventory> poInventory) {
//		UsedItemsGroup[] usedItemsGroups = new UsedItemsGroup[poInventory.size()];
//		int i = 0;
//		for(ProcessItemInventory processItemRow: poInventory) {
//			UsedItem[] usedItems = new UsedItem[processItemRow.getStorageForms().size()];
//			int j = 0;
//			for(StorageInventoryRow storagesRow: processItemRow.getStorageForms()) {
//				usedItems[j] = new UsedItem();
//				Storage storage = new Storage();
//				usedItems[j].setStorage(storage);
//				storage.setId(storagesRow.getId());
//				storage.setVersion(storagesRow.getVersion());
//				usedItems[j].setNumberUsedUnits(storagesRow.getNumberUnits());
//				j++;
//			}
//			usedItemsGroups[i] = new UsedItemsGroup();
//			usedItemsGroups[i].setUsedItems(usedItems);
//			i++;
//
//		}
//		return usedItemsGroups;
//	}	
	public static List<UsedItemsGroupDTO> getUsedItemsGroupsDTOs(List<ProcessItemInventory> poInventory) {
		List<UsedItemsGroupDTO> usedItemsGroups = new ArrayList<UsedItemsGroupDTO>();
		for(ProcessItemInventory processItemRow: poInventory) {
			List<UsedItemDTO> usedItems = new ArrayList<UsedItemDTO>();
			for(StorageInventoryRow storagesRow: processItemRow.getStorageForms()) {
				UsedItemDTO usedItem = new UsedItemDTO();
				StorageDTO storage = new StorageDTO();
				usedItem.setStorage(storage);
				storage.setId(storagesRow.getId());
				storage.setVersion(storagesRow.getVersion());
				usedItem.setNumberUsedUnits(storagesRow.getNumberUnits());
				usedItems.add(usedItem);
			}
			UsedItemsGroupDTO usedItemsGroup = new UsedItemsGroupDTO();
			usedItemsGroup.setUsedItems(usedItems);
			usedItemsGroups.add(usedItemsGroup);

		}
		return usedItemsGroups;
	}

//	public StorageMovesGroup[] getStorageMoves(List<ProcessItemInventory> poInventory) {
//		StorageMovesGroup[] storageMovesGroups = new StorageMovesGroup[poInventory.size()];
//		int i = 0;
//		for(ProcessItemInventory processItemRow: poInventory) {
//			StorageMove[] storageMoves = new StorageMove[processItemRow.getStorageForms().size()];
//			int j = 0;
//			for(StorageInventoryRow storagesRow: processItemRow.getStorageForms()) {
//				storageMoves[j] = new StorageMove();
//				Storage storage = new Storage();
//				storageMoves[j].setStorage(storage);
//				storage.setId(storagesRow.getId());
//				storage.setVersion(storagesRow.getVersion());
//				storageMoves[j].setNumberUsedUnits(storagesRow.getNumberUnits());
//				storageMoves[j].setUnitAmount(storagesRow.getUnitAmount());
//				storageMoves[j].setNumberUnits(storagesRow.getNumberUnits());
////				storageMoves[j].setAccessWeight(storagesRow.getAccessWeight());
//				storageMoves[j].setWarehouseLocation(getWarehouse());
//				j++;
//			}
//			storageMovesGroups[i] = new StorageMovesGroup();
////			storageMovesGroups[i].setMeasureUnit(processItemRow.getItem().getMeasureUnit());
//			storageMovesGroups[i].setStorageMoves(storageMoves);
//			i++;
//
//		}
//		return storageMovesGroups;
//	}
	public List<StorageMovesGroupDTO> getStorageMovesDTOs(List<ProcessItemInventory> poInventory) {
		List<StorageMovesGroupDTO> storageMovesGroups = new ArrayList<StorageMovesGroupDTO>();
		for(ProcessItemInventory processItemRow: poInventory) {
			StorageMovesGroupDTO storageMovesGroup = new StorageMovesGroupDTO();
			storageMovesGroups.add(storageMovesGroup);
			List<StorageMoveDTO> storageMoves = new ArrayList<StorageMoveDTO>();
			for(StorageInventoryRow storagesRow: processItemRow.getStorageForms()) {
				StorageMoveDTO storageMove = new StorageMoveDTO();
				storageMoves.add(storageMove);
				StorageBaseDTO storage = new StorageBaseDTO();
				storageMove.setStorage(storage);
				storage.setId(storagesRow.getId());
				storage.setVersion(storagesRow.getVersion());
				storageMove.setNumberUsedUnits(storagesRow.getNumberUnits());
				storageMove.setUnitAmount(storagesRow.getUnitAmount());
				storageMove.setNumberUnits(storagesRow.getNumberUnits());
				storageMove.setWarehouseLocation(getWarehouse());
			}
			storageMovesGroup.setStorageMoves(storageMoves);

		}
		return storageMovesGroups;
	}
	
//	public ItemCount[] getItemCounts(List<ProcessItemInventory> poInventory) {
//		ItemCount[] itemCounts = new ItemCount[poInventory.size()];
//		CountAmount[] countAmounts;
//		for(int i=0; i<itemCounts.length; i++) {
//			//build item count
//			ProcessItemInventory processItemRow = poInventory.get(i);
//			itemCounts[i] = new ItemCount();
//			Item item = getItem(processItemRow.getItem());
//			itemCounts[i].setItem(item);
//			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
//			StorageInventoryRow randStorage = storagesRows.get(0);
//			itemCounts[i].setMeasureUnit(randStorage.getTotalBalance().getMeasureUnit());
////			itemCounts[i].setContainerWeight(randStorage.getAccessWeight());
//			countAmounts = new CountAmount[storagesRows.size()];
//			int j=0;
//			for(StorageInventoryRow storageRow: storagesRows) {
//				countAmounts[j] = new CountAmount();
//				countAmounts[j].setAmount(storageRow.getTotalBalance().getAmount());
//				countAmounts[j].setOrdinal((storageRow.getOrdinal()));
//				
//				j++;
//			}
//			
//			itemCounts[i].setAmounts(countAmounts);
//		}
//		return itemCounts;
//	}
	public List<ItemCountDTO> getItemCounts(List<ProcessItemInventory> poInventory) {
		List<ItemCountDTO> itemCounts = new ArrayList<ItemCountDTO>();
		for(ProcessItemInventory processItemRow: poInventory) {
			//build item count
			ItemCountDTO itemCount = new ItemCountDTO();
			itemCounts.add(itemCount);
			List<CountAmountDTO> countAmounts = new ArrayList<CountAmountDTO>();
			Item item = new Item();
			item.setId(processItemRow.getItem().getId());
			itemCount.setItem(new ItemWithUse(item));
			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
			StorageInventoryRow randStorage = storagesRows.get(0);
			itemCount.setMeasureUnit(randStorage.getTotalBalance().getMeasureUnit());
//			itemCount.setContainerWeight(randStorage.getAccessWeight());
			for(StorageInventoryRow storageRow: storagesRows) {
				CountAmountDTO countAmount = new CountAmountDTO();
				countAmounts.add(countAmount);
				countAmount.setAmount(storageRow.getTotalBalance().getAmount());
				countAmount.setOrdinal((storageRow.getOrdinal()));
			}			
			itemCount.setAmounts(countAmounts);
		}
		return itemCounts;
	}

//	public ProcessItem[] getProcessItems(List<ProcessItemInventory> poInventory) {
//		ProcessItem[] processItems = new ProcessItem[poInventory.size()];
//		Storage[] storageForms;
//		for(int i=0; i<processItems.length; i++) {
//			//build process item
//			ProcessItemInventory processItemRow = poInventory.get(i);
//			processItems[i] = new ProcessItem();
//			Item item = getItem(processItemRow.getItem());
//			processItems[i].setItem(item);
//			processItems[i].setMeasureUnit(item.getMeasureUnit());
//			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
//			storageForms = new Storage[storagesRows.size()];
//			int j=0;
//			for(StorageInventoryRow storageRow: storagesRows) {
//				storageForms[j] = new Storage();
//				storageForms[j].setUnitAmount(storageRow.getUnitAmount());
//				storageForms[j].setNumberUnits(storageRow.getNumberUnits());
//				storageForms[j].setWarehouseLocation(getWarehouse());
//				
//				j++;
//			}
//			
//			processItems[i].setStorageForms(storageForms);
//		}
//		return processItems;
//	}
	public List<ProcessItemDTO> getProcessItemsDTOs(List<ProcessItemInventory> poInventory) {
		List<ProcessItemDTO> processItems = new ArrayList<>();
		List<StorageDTO> storageForms;
		for(ProcessItemInventory processItemRow: poInventory) {
			//build process item
			ProcessItemDTO processItem = new ProcessItemDTO();
			processItems.add(processItem);
			processItem.setItem(processItemRow.getItem());
			processItem.setMeasureUnit(processItemRow.getItem().getMeasureUnit());
			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
			storageForms = new ArrayList<StorageDTO>();
			int j=0;
			for(StorageInventoryRow storageRow: storagesRows) {
				StorageDTO storage = new StorageDTO();
				storageForms.add(storage);
				storage.setUnitAmount(storageRow.getUnitAmount());
				storage.setNumberUnits(storageRow.getNumberUnits());
				storage.setWarehouseLocation(getWarehouse());
				
				j++;
			}			
			processItem.setStorageForms(storageForms);
		}
		return processItems;
	}
	
	public Item getItem(ItemWithUnitDTO item) {
		return getItem(item.getId(), item.getMeasureUnit(), item.getClazz());
	}
	
	public Item getItem(ItemDTO item) {
		return getItem(item.getId(), item.getMeasureUnit(), item.getClazz());
	}

	
	public Item getItem(Integer id, MeasureUnit measureUnit, Class<? extends Item> clazz) {
		Item item;
		if(clazz == Item.class) {
			item = new Item();
		}
		else if(clazz == CashewItem.class) {
			item = new CashewItem();
		}
		else {
			throw new NullPointerException();
		}
		item.setMeasureUnit(measureUnit);
		item.setId(id);
		return item;
	}
	
	public ProductPoCode getPoCode() {
		ProductPoCode poCode = new ProductPoCode();
		poCode.setId(getPoCodeBasic().getId());
		return poCode;
	}

	
	public WeightedPo[] getProductWeightedPos(int size) {
		
		WeightedPo[] productWeightedPos = new WeightedPo[size];
		for(int i=0; i < productWeightedPos.length; i++) {
			productWeightedPos[i] = new WeightedPo();
			productWeightedPos[i].setPoCode(getPoCode());
			productWeightedPos[i].setWeight(new BigDecimal((1)/(Double.valueOf(productWeightedPos.length))));
			
		}
				
		return productWeightedPos;
	}


}
