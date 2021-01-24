/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.processinfo.OrderItemDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.PackedItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ShipmentCode;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageWithSample;
import com.avc.mis.beta.entities.process.inventory.UsedItem;
import com.avc.mis.beta.entities.processinfo.CountAmount;
import com.avc.mis.beta.entities.processinfo.ItemCount;
import com.avc.mis.beta.entities.processinfo.OrderItem;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.ProductWeightedPo;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;

/**
 * @author Zvi
 *
 */
@Service
public class TestService {
	
	@Autowired private Suppliers suppliers;	
	@Autowired ValueTablesReader valueTableReader;
	@Autowired ObjectTablesReader objectTablesReader;
	@Autowired Orders orders;
	@Autowired Receipts receipts;
	
	private int randCode = LocalDateTime.now().hashCode();
	private Random randNum = new Random();
	
	public Supplier addBasicSupplier() {
		Supplier supplier = new Supplier();
		supplier.setName("service supplier " + randCode++);
		suppliers.addSupplier(supplier);
		return supplier;
	}

	PoCode addPoCode() {
		PoCode poCode = new PoCode();
		poCode.setCode(Integer.toString(randCode++));
		Supplier supplier = addBasicSupplier();
		poCode.setSupplier(supplier);
		poCode.setContractType(getContractType());
		orders.addPoCode(poCode);
		return poCode;
	}

	public ShipmentCode getShipmentCode() {
		ShipmentCode shipmentCode = new ShipmentCode();
		shipmentCode.setCode(Integer.toString(randCode++));

		return shipmentCode;
	}

	
	public PO addBasicCashewOrder() {
		
		//build purchase order
		PO po = new PO();
		PoCode poCode = addPoCode();
		po.setPoCode(poCode);
//		poCode.setCode(Integer.toString(randCode++));
//		Supplier supplier = addBasicSupplier();
//		poCode.setSupplier(supplier);
//		poCode.setContractType(getContractType());
		
		//build process
		po.setRecordedTime(OffsetDateTime.now());
		
		//add order items
		OrderItem[] items = getOrderItems(OrdersTest.NUM_ITEMS);				
		po.setOrderItems(items);
		orders.addCashewOrder(po);
		return po;
	}
	
	private OrderItem[] getOrderItems(int numOfItems) {
		OrderItem[] orderItems = new OrderItem[numOfItems];
		List<Item> items = getItems();
		for(int i=0; i<orderItems.length; i++) {
			orderItems[i] = new OrderItem();
			Item item = items.get(randNum.nextInt(items.size()));
			orderItems[i].setItem(item);
			orderItems[i].setNumberUnits(new AmountWithUnit(new BigDecimal(i+1), item.getMeasureUnit()));
			orderItems[i].setUnitPrice(new AmountWithCurrency("1.16", "USD"));
			orderItems[i].setDeliveryDate("1983-11-23");
		}
		return orderItems;
	}
	
	public Receipt addOneItemCashewReceipt() {
		//build order receipt
		Receipt receipt = new Receipt();
		PoCode poCode = addPoCode();
//		poCode.setCode(Integer.toString(randCode++));
//		Supplier supplier = addBasicSupplier();
//		poCode.setSupplier(supplier);
//		poCode.setContractType(getContractType());
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(OffsetDateTime.now());
		//add order items
		receipt.setReceiptItems(getReceiptItems(1));
		receipts.addCashewReceipt(receipt);
		return receipt;
	}
	
	public Receipt addBasicCashewReceipt() {
		//build order receipt
		Receipt receipt = new Receipt();
		PoCode poCode = addPoCode();
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(OffsetDateTime.now());
		receipt.setStartTime(LocalTime.parse("20:15"));
		//add order items
		receipt.setReceiptItems(getReceiptItems(OrdersTest.NUM_ITEMS));
		receipts.addCashewReceipt(receipt);
		return receipt;
	}

	private ReceiptItem[] getReceiptItems(int numOfItems) {
		ReceiptItem[] receiptItems = new ReceiptItem[numOfItems];
		StorageWithSample[] storageForms = new StorageWithSample[receiptItems.length];
		ExtraAdded[] added = new ExtraAdded[receiptItems.length];
		List<Item> items = getItems();
		Warehouse storage = getWarehouse();
		for(int i=0; i<receiptItems.length; i++) {
			storageForms[i] = new StorageWithSample();
			storageForms[i].setUnitAmount(BigDecimal.ONE);
//			storageForms[i].setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(1), "LBS"));
			storageForms[i].setNumberUnits(BigDecimal.valueOf(35000));
			storageForms[i].setWarehouseLocation(storage);
//			storageForms[i].setSampleContainerWeight(BigDecimal.valueOf(0.002));
			storageForms[i].setNumberOfSamples(BigInteger.valueOf(30));
			storageForms[i].setAvgTestedWeight(BigDecimal.valueOf(50.01));
			//build receipt item
			receiptItems[i] = new ReceiptItem();
			Item item = items.get(randNum.nextInt(items.size()));
			receiptItems[i].setItem(item);
			receiptItems[i].setReceivedOrderUnits(new AmountWithUnit(BigDecimal.valueOf(35000), MeasureUnit.LBS));
			receiptItems[i].setMeasureUnit(item.getMeasureUnit());
			receiptItems[i].setUnitPrice(new AmountWithCurrency("2.99", "USD"));
			receiptItems[i].setStorageForms(new Storage[] {storageForms[i]});
			//add extra bonus
			added[i] = new ExtraAdded();
			added[i].setUnitAmount(BigDecimal.ONE);//because database is set to scale 2
			added[i].setNumberUnits(new BigDecimal(4).setScale(2));
			receiptItems[i].setExtraAdded(new ExtraAdded[] {added[i]});
		}
		return receiptItems;
	}
	
	public Receipt getCashewOrderReceipt(int orderPoCode) {
		//build order receipt
		Receipt receipt = new Receipt();
		PoCode poCode = new PoCode();
		poCode.setId(orderPoCode);
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(OffsetDateTime.now());
		//add order items
		PoDTO poDTO = orders.getOrder(orderPoCode);
		receipt.setReceiptItems(getOrderReceiptItems(poDTO));
		receipts.addCashewOrderReceipt(receipt);
		return receipt;		
	}
	
	private ReceiptItem[] getOrderReceiptItems(PoDTO poDTO) {
		List<OrderItemDTO> orderItems = poDTO.getOrderItems();

		ReceiptItem[] items = new ReceiptItem[orderItems.size()];
		StorageWithSample[] storageForms = new StorageWithSample[items.length];
		Warehouse storage = getWarehouse();
		OrderItem oi;
		int i=0;
		for(OrderItemDTO oItem: orderItems) {
			items[i] = new ReceiptItem();
			storageForms[i] = new StorageWithSample();
			int itemId = oItem.getItem().getId();
			Item item = getItems().stream().filter(j -> j.getId() == itemId).findAny().get();
			items[i].setItem(item);
			items[i].setReceivedOrderUnits(new AmountWithUnit(BigDecimal.valueOf(35000), MeasureUnit.LBS));
			items[i].setMeasureUnit(item.getMeasureUnit());
			items[i].setUnitPrice(new AmountWithCurrency("2.99", "USD"));
			storageForms[i].setUnitAmount(BigDecimal.ONE);
			storageForms[i].setNumberUnits(BigDecimal.valueOf(35000));
			storageForms[i].setWarehouseLocation(storage);
//			storageForms[i].setSampleContainerWeight(BigDecimal.valueOf(0.002));
			storageForms[i].setNumberOfSamples(BigInteger.valueOf(30));
			storageForms[i].setAvgTestedWeight(BigDecimal.valueOf(50.01));
			items[i].setStorageForms(new StorageWithSample[] {storageForms[i]});
			oi  = new OrderItem();
			oi.setId(oItem.getId());
			oi.setVersion(oItem.getVersion());
			items[i].setOrderItem(oi);
			items[i].setExtraRequested(new AmountWithUnit(BigDecimal.valueOf(200), "KG"));
			i++;
		}
		return items;
	}
	
	public Warehouse getWarehouse() {
		List<Warehouse> warehouses = valueTableReader.getAllWarehouses();
		if(warehouses.isEmpty())
			fail("No warehouses in database for running this test");
		return warehouses.get(randNum.nextInt(warehouses.size()));
	}
	
	public PoCodeBasic getPoCodeBasic() {
		List<PoCodeBasic> poCodes = objectTablesReader.findAllPoCodes();
		if(poCodes.isEmpty())
			fail("No po codes in database for running this test");
		return poCodes.get(randNum.nextInt(poCodes.size()));
	}
	
	

	public List<Item> getItems() {
		List<Item> items = valueTableReader.getAllItems();
		if(items.isEmpty())
			fail("No items in database for running this test");
		return items;
		
	}
	
	public Item getItem() {
		List<Item> items = getItems();
		return items.get(randNum.nextInt(items.size()));
	}
	
	private ContractType getContractType() {
		List<ContractType> contractTypes = valueTableReader.getAllContractTypes();
		if(contractTypes.isEmpty())
			fail("No Contract Types in database for running this test");
		return contractTypes.get(randNum.nextInt(contractTypes.size()));
	}
	
	public City getCity() {
		List<City> cities = valueTableReader.getAllCities();
		if(cities.isEmpty())
			fail("No Cities in database");
		return cities.get(randNum.nextInt(cities.size()));
	}
	
	public BankBranch getBankBranch() {
		List<BankBranch> branches = valueTableReader.getAllBankBranches();
		if(branches.isEmpty())
			fail("No Bank Branches in database");
		return branches.get(randNum.nextInt(branches.size()));
	}
	
	public List<SupplyCategory> getSupplyCategories() {
		List<SupplyCategory> supplyCategories = valueTableReader.getAllSupplyCategories();
		if(supplyCategories.isEmpty())
			fail("No Supply Categories in database");
		return supplyCategories;
	}

	public Object getSupplycategory() {
		List<SupplyCategory> supplyCategories = getSupplyCategories();
		return supplyCategories.get(randNum.nextInt(supplyCategories.size()));
	}
	
	public ShippingPort getShippingPort() {
		List<ShippingPort> ports = valueTableReader.getAllShippingPorts();
		if(ports.isEmpty())
			fail("No Shipping Ports in database");
		return ports.get(randNum.nextInt(ports.size()));
	}

	public void cleanup(Supplier supplier) {
		suppliers.permenentlyRemoveSupplier(supplier.getId());		
	}
	
	public void cleanup(PO po) {
		BasePoCode poCode = po.getPoCode();
		Supplier supplier = poCode.getSupplier();
		orders.removeOrder(po.getId());
		suppliers.permenentlyRemoveEntity(poCode);
		cleanup(supplier);
		
	}

	public void cleanup(Receipt receipt) {
		receipts.removeReceipt(receipt.getId());
	}
	
	public static UsedItemsGroup[] getUsedItemsGroups(List<ProcessItemInventory> poInventory) {
		UsedItemsGroup[] usedItemsGroups = new UsedItemsGroup[poInventory.size()];
		int i = 0;
		for(ProcessItemInventory processItemRow: poInventory) {
			UsedItem[] usedItems = new UsedItem[processItemRow.getStorageForms().size()];
			int j = 0;
			for(StorageInventoryRow storagesRow: processItemRow.getStorageForms()) {
				usedItems[j] = new UsedItem();
				Storage storage = new Storage();
				usedItems[j].setStorage(storage);
				storage.setId(storagesRow.getId());
				storage.setVersion(storagesRow.getVersion());
				usedItems[j].setNumberUsedUnits(storagesRow.getNumberUnits());
				j++;
			}
			usedItemsGroups[i] = new UsedItemsGroup();
			usedItemsGroups[i].setUsedItems(usedItems);
			i++;

		}
		return usedItemsGroups;
	}
	
	/**
	 * @param poInventory
	 * @return
	 */
	public ItemCount[] getItemCounts(List<ProcessItemInventory> poInventory) {
		ItemCount[] itemCounts = new ItemCount[poInventory.size()];
		CountAmount[] countAmounts;
		for(int i=0; i<itemCounts.length; i++) {
			//build item count
			ProcessItemInventory processItemRow = poInventory.get(i);
			itemCounts[i] = new ItemCount();
			Item item = getItem(processItemRow.getItem());
			itemCounts[i].setItem(item);
			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
			StorageInventoryRow randStorage = storagesRows.get(0);
			itemCounts[i].setMeasureUnit(randStorage.getTotalBalance().getMeasureUnit());
			itemCounts[i].setContainerWeight(randStorage.getAccessWeight());
			countAmounts = new CountAmount[storagesRows.size()];
			int j=0;
			for(StorageInventoryRow storageRow: storagesRows) {
				countAmounts[j] = new CountAmount();
				countAmounts[j].setAmount(storageRow.getTotalBalance().getAmount());
				countAmounts[j].setOrdinal((storageRow.getOrdinal()));
				
				j++;
			}
			
			itemCounts[i].setAmounts(countAmounts);
		}
		return itemCounts;
	}

	public ProcessItem[] getProcessItems(List<ProcessItemInventory> poInventory) {
		ProcessItem[] processItems = new ProcessItem[poInventory.size()];
		Storage[] storageForms;
		for(int i=0; i<processItems.length; i++) {
			//build process item
			ProcessItemInventory processItemRow = poInventory.get(i);
			processItems[i] = new ProcessItem();
			Item item = getItem(processItemRow.getItem());
			processItems[i].setItem(item);
			processItems[i].setMeasureUnit(item.getMeasureUnit());
			List<StorageInventoryRow> storagesRows = processItemRow.getStorageForms();
			storageForms = new Storage[storagesRows.size()];
			int j=0;
			for(StorageInventoryRow storageRow: storagesRows) {
				storageForms[j] = new Storage();
				storageForms[j].setUnitAmount(storageRow.getUnitAmount());
				storageForms[j].setNumberUnits(storageRow.getNumberUnits());
				storageForms[j].setWarehouseLocation(getWarehouse());
				
				j++;
			}
			
			processItems[i].setStorageForms(storageForms);
		}
		return processItems;
	}
	
	public Item getItem(ItemDTO itemDTO) {
		Item item;
		if(itemDTO.getClazz() == BulkItem.class) {
			item = new BulkItem(itemDTO.getMeasureUnit());			
		}
		else if(itemDTO.getClazz() == PackedItem.class) {
			item = new PackedItem();
		}
		else {
			throw new NullPointerException();
		}
		item.setId(itemDTO.getId());
		return item;
	}
	
	public PoCode getPoCode() {
		PoCode poCode = new PoCode();
		poCode.setId(getPoCodeBasic().getId());
		return poCode;
	}

	
	public ProductWeightedPo[] getProductWeightedPos(int size) {
		
		ProductWeightedPo[] productWeightedPos = new ProductWeightedPo[size];
		for(int i=0; i < productWeightedPos.length; i++) {
			productWeightedPos[i] = new ProductWeightedPo();
			productWeightedPos[i].setPoCode(getPoCode());
			productWeightedPos[i].setWeight(new BigDecimal((i+1)/(Double.valueOf(productWeightedPos.length))));
			
		}
				
		return productWeightedPos;
	}


}
