/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.collection.OrderItemDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.CashewItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.ContainerBooking;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ShipmentCode;
import com.avc.mis.beta.entities.process.collection.CountAmount;
import com.avc.mis.beta.entities.process.collection.ItemCount;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.collection.ReceiptItem;
import com.avc.mis.beta.entities.process.collection.UsedItemsGroup;
import com.avc.mis.beta.entities.process.collection.WeightedPo;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageWithSample;
import com.avc.mis.beta.entities.process.inventory.UsedItem;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.service.ContainerBookings;
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
	@Autowired ContainerBookings bookings;
	
	private int randCode = LocalDateTime.now().hashCode();
	private Random randNum = new Random();
	
	public Supplier addBasicSupplier() {
		Supplier supplier = new Supplier();
		supplier.setName("service supplier " + randCode++);
		suppliers.addSupplier(supplier);
		return supplier;
	}
	
	public ContainerBooking addBasicContainerBooking() {
		ContainerBooking booking = new ContainerBooking();
		booking.setBookingNumber("booking_no " + randCode++);
		booking.setBookingDate("1983-11-23");
		booking.setRecordedTime(LocalDateTime.now());
		return booking;
	}
	
	public ShipingDetails getShipingDetails() {
		ShipingDetails shipingDetails = new ShipingDetails();
		shipingDetails.setEtd("2007-12-03");
		shipingDetails.setEta("2008-12-03");
		return shipingDetails;
	}

	PoCode addPoCode() {
		PoCode poCode = new PoCode();
		poCode.setCode(Integer.toString(randCode++));
		Supplier supplier = addBasicSupplier();
		poCode.setSupplier(supplier);
		poCode.setContractType(getContractType());
		objectWriter.addPoCode(poCode);
		return poCode;
	}
	
	GeneralPoCode addGeneralPoCode() {
		GeneralPoCode poCode = new GeneralPoCode();
		poCode.setCode(Integer.toString(randCode++));
		Supplier supplier = addBasicSupplier();
		poCode.setCode(Integer.toString(randCode++));
		poCode.setSupplier(supplier);
		poCode.setContractType(getContractType());
		objectWriter.addPoCode(poCode);
		return poCode;
	}

	public ShipmentCode addShipmentCode() {
		ShipmentCode shipmentCode = new ShipmentCode();
		shipmentCode.setCode(Integer.toString(randCode++));
		shipmentCode.setPortOfDischarge(getShippingPort());
		objectWriter.addShipmentCode(shipmentCode);
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
		po.setRecordedTime(LocalDateTime.now());
		
		//add order items
		OrderItem[] items = getOrderItems(OrdersTest.NUM_ITEMS, ItemGroup.PRODUCT);				
		po.setOrderItems(items);
		orders.addCashewOrder(po);
		return po;
	}
	
	public PO addBasicGeneralOrder() {
		
		//build purchase order
		PO po = new PO();
		GeneralPoCode poCode = addGeneralPoCode();
//		Supplier supplier = addBasicSupplier();
//		poCode.setCode(Integer.toString(randCode++));
//		poCode.setSupplier(supplier);
//		poCode.setContractType(getContractType());
		po.setPoCode(poCode);
		
		//build process
		po.setRecordedTime(LocalDateTime.now());
		
		//add order items
		OrderItem[] items = getOrderItems(OrdersTest.NUM_ITEMS, ItemGroup.GENERAL);				
		po.setOrderItems(items);
		orders.addGeneralOrder(po);
		return po;
	}
	
	private OrderItem[] getOrderItems(int numOfItems, ItemGroup group) {
		OrderItem[] orderItems = new OrderItem[numOfItems];
		for(int i=0; i<orderItems.length; i++) {
			orderItems[i] = new OrderItem();
			Item item = getItemByGroup(group);
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
		receipt.setRecordedTime(LocalDateTime.now());
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
		receipt.setRecordedTime(LocalDateTime.now());
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
			Item item = getItem();
			receiptItems[i].setItem(item);
			receiptItems[i].setReceivedOrderUnits(new AmountWithUnit(BigDecimal.valueOf(35000), item.getMeasureUnit()));
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
		receipt.setRecordedTime(LocalDateTime.now());
		//add order items
		PoDTO poDTO = orders.getOrder(orderPoCode);
		receipt.setReceiptItems(getOrderReceiptItems(poDTO));
		receipts.addCashewOrderReceipt(receipt);
		return receipt;		
	}
	
	public Receipt getGeneralOrderReceipt(int orderPoCode) {
		//build order receipt
		Receipt receipt = new Receipt();
		PoCode poCode = new PoCode();
		poCode.setId(orderPoCode);
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(LocalDateTime.now());
		//add order items
		PoDTO poDTO = orders.getOrder(orderPoCode);
		receipt.setReceiptItems(getOrderReceiptItems(poDTO));
		receipts.addGeneralOrderReceipt(receipt);
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
			ItemDTO item = getItemsByGroup(null).stream().filter(j -> j.getId() == itemId).findAny().get();
			items[i].setItem(getItem(item));
			items[i].setReceivedOrderUnits(new AmountWithUnit(BigDecimal.valueOf(35000), item.getMeasureUnit()));
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
	
	public ProductionLine getProductionLine(ProductionFunctionality functionality) {
		List<ProductionLine> productionLines = valueTableReader.getProductionLinesByFuncionality(new ProductionFunctionality[] {functionality});
		if(productionLines.isEmpty())
			fail("No production Lines in database");
		return productionLines.get(randNum.nextInt(productionLines.size()));
	}

	public void cleanup(Supplier supplier) {
		suppliers.permenentlyRemoveSupplier(supplier.getId());		
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

	public void cleanup(Receipt receipt) {
		processInfoWriter.removeProcess(receipt.getId());
//		receipts.removeReceipt(receipt.getId());
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
//			itemCounts[i].setContainerWeight(randStorage.getAccessWeight());
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
	
	public PoCode getPoCode() {
		PoCode poCode = new PoCode();
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
