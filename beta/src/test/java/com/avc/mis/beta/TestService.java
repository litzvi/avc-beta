/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.processinfo.OrderItemDTO;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.processinfo.ExtraAdded;
import com.avc.mis.beta.entities.processinfo.OrderItem;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Orders;
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
	
	public PO addBasicCashewOrder() {
		
		//build purchase order
		PO po = new PO();
		PoCode poCode = new PoCode();
		po.setPoCode(poCode);
		poCode.setId(randCode++);
		Supplier supplier = addBasicSupplier();
		poCode.setSupplier(supplier);
		poCode.setContractType(getContractType());
		
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
			orderItems[i].setItem(items.get(randNum.nextInt(items.size())));
			orderItems[i].setNumberUnits(new AmountWithUnit(new BigDecimal(i+1), "KG"));
			orderItems[i].setUnitPrice(new AmountWithCurrency("1.16", "USD"));
			orderItems[i].setDeliveryDate("1983-11-23");
		}
		return orderItems;
	}
	
	public Receipt addBasicCashewReceipt() {
		//build order receipt
		Receipt receipt = new Receipt();
		PoCode poCode = new PoCode();
		poCode.setCode(randCode++);
		Supplier supplier = addBasicSupplier();
		poCode.setSupplier(supplier);
		poCode.setContractType(getContractType());
		receipt.setPoCode(poCode);
		//build process
		receipt.setRecordedTime(OffsetDateTime.now());
		//add order items
		receipt.setReceiptItems(getReceiptItems(OrdersTest.NUM_ITEMS));
		receipts.addCashewReceipt(receipt);
		return receipt;
	}
	

	private ReceiptItem[] getReceiptItems(int numOfItems) {
		ReceiptItem[] receiptItems = new ReceiptItem[numOfItems];
		Storage[] storageForms = new Storage[receiptItems.length];
		ExtraAdded[] added = new ExtraAdded[receiptItems.length];
		List<Item> items = getItems();
		Warehouse storage = getWarehouse();
		for(int i=0; i<receiptItems.length; i++) {
			storageForms[i] = new Storage();
			storageForms[i].setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(1), "LBS"));
			storageForms[i].setNumberUnits(BigDecimal.valueOf(35000));
			storageForms[i].setWarehouseLocation(storage);
			//build receipt item
			receiptItems[i] = new ReceiptItem();
			receiptItems[i].setItem(items.get(randNum.nextInt(items.size())));
			receiptItems[i].setStorageForms(new Storage[] {storageForms[i]});
			//add extra bonus
			added[i] = new ExtraAdded();
			added[i].setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(1), "KG"));//because database is set to scale 2
			added[i].setNumberUnits(new BigDecimal(4).setScale(2));
			receiptItems[i].setExtraAdded(new ExtraAdded[] {added[i]});
		}
		return receiptItems;
	}
	
	public Receipt getCashewOrderReceipt(int orderPoCode) {
		//build order receipt
		Receipt receipt = new Receipt();
		PoCode poCode = new PoCode();
		poCode.setCode(orderPoCode);
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
		Set<OrderItemDTO> orderItems = poDTO.getOrderItems();
		ReceiptItem[] items = new ReceiptItem[orderItems.size()];
		Storage[] storageForms = new Storage[items.length];
		Warehouse storage = getWarehouse();
		OrderItem oi;
		int i=0;
		for(OrderItemDTO oItem: orderItems) {
			items[i] = new ReceiptItem();
			storageForms[i] = new Storage();
			Item item = new Item();
			item.setId(oItem.getItem().getId());
			items[i].setItem(item);
			storageForms[i].setUnitAmount(new AmountWithUnit(BigDecimal.valueOf(1), "LBS"));
			storageForms[i].setNumberUnits(BigDecimal.valueOf(35000));
			storageForms[i].setWarehouseLocation(storage);
			items[i].setStorageForms(new Storage[] {storageForms[i]});
			oi  = new OrderItem();
			oi.setId(oItem.getId());
			oi.setVersion(oItem.getVersion());
			items[i].setOrderItem(oi);
			items[i].setExtraRequested(new AmountWithUnit(BigDecimal.valueOf(200)));
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

	private List<Item> getItems() {
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

	public void cleanup(Supplier supplier) {
		suppliers.permenentlyRemoveSupplier(supplier.getId());		
	}
	
	public void cleanup(PO po) {
		PoCode poCode = po.getPoCode();
		Supplier supplier = poCode.getSupplier();
		orders.removeOrder(po.getId());
		suppliers.permenentlyRemoveEntity(poCode);
		cleanup(supplier);
		
	}

	public void cleanup(Receipt receipt) {
		receipts.removeReceipt(receipt.getId());
	}

}
