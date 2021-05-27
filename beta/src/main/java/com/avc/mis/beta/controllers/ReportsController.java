package com.avc.mis.beta.controllers;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.report.FinalReport;
import com.avc.mis.beta.dto.values.CashewItemDTO;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.PackageType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.service.ContainerArrivals;
import com.avc.mis.beta.service.Loading;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessReader;
import com.avc.mis.beta.service.ProcessSummaryReader;
import com.avc.mis.beta.service.ProductionProcesses;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.Receipts;
import com.avc.mis.beta.service.Samples;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.WarehouseManagement;
import com.avc.mis.beta.service.report.InventoryReports;
import com.avc.mis.beta.service.report.LoadingReports;
import com.avc.mis.beta.service.report.row.CashewBaggedInventoryRow;
import com.avc.mis.beta.service.report.row.CashewExportReportRow;
import com.avc.mis.beta.service.report.row.FinishedProductInventoryRow;
import com.avc.mis.beta.service.report.row.ReceiptInventoryRow;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/reports")
public class ReportsController {
	
	@Autowired
	private Orders ordersDao;
	
	@Autowired
	private Receipts receipts;
	
	@Autowired
	private Samples samplesControl;
	
	@Autowired
	private QualityChecks qualityChecks;
	
	@Autowired
	private ProcessReader processReader;
	
	@Autowired
	private ProcessSummaryReader processSummaryReader;
	
	@Autowired
	private Loading loading;
	
	@Autowired
	private InventoryReports inventoryReports;
	
	@Autowired
	private LoadingReports loadingReports;
	
	@Autowired
	private ContainerArrivals containerArrivals;
	
	@Autowired
	private ProcessInfoReader processInfoReader;
	
	@Autowired
	private ObjectTablesReader objectTableReader;
	
	@Autowired
	private ValueTablesReader valueTablesReader;
	
	@Autowired
	private WarehouseManagement warehouseManagement;
	
	@Autowired
	private ProductionProcesses productionProcesses;
	
	
	@RequestMapping("/getAllProcesses/{id}")
	public Map<String, List<Object>> getAllProcesses(@PathVariable("id") int poCode) {
		List<Object> orders = new ArrayList<Object>();
		List<Object> reciving = new ArrayList<Object>();
		List<Object> qcsTested = new ArrayList<Object>();
//		List<Object> transferProcces = new ArrayList<Object>();
		List<Object> relocationProcces = new ArrayList<Object>();
		List<Object> cleaningProcces = new ArrayList<Object>();
		List<Object> roastingProcces = new ArrayList<Object>();
		List<Object> packingProcces = new ArrayList<Object>();
		List<Object> arrivalProcces = new ArrayList<Object>();
		List<Object> loadingProcces = new ArrayList<Object>();
		processReader.getAllProcessesByPo(poCode).forEach(k -> {
			switch (k.getProcessName()) {
				case CASHEW_ORDER:
				case GENERAL_ORDER:
					orders.add(ordersDao.getOrderByProcessId(k.getId()));
					break;
				case CASHEW_RECEIPT:
				case GENERAL_RECEIPT:
					reciving.add(receipts.getReceiptByProcessId(k.getId()));
					break;
				case CASHEW_RECEIPT_QC:
				case VINA_CONTROL_QC:
				case SAMPLE_QC:
				case SUPPLIER_QC:
				case ROASTED_CASHEW_QC:
					qcsTested.add(qualityChecks.getQcByProcessId(k.getId()));
					break;
//				case STORAGE_TRANSFER:
//					transferProcces.add(warehouseManagement.getStorageTransfer(k.getId()));
//					break;
				case STORAGE_RELOCATION:
					relocationProcces.add(warehouseManagement.getStorageRelocation(k.getId()));
					break;
				case CASHEW_CLEANING:
					cleaningProcces.add(productionProcesses.getProductionProcess(k.getId()));
					break;
				case CASHEW_ROASTING:
					roastingProcces.add(productionProcesses.getProductionProcess(k.getId()));
					break;
				case PACKING:
					packingProcces.add(productionProcesses.getProductionProcess(k.getId()));
					break;
				case CONTAINER_ARRIVAL:
//					arrivalProcces.add(containerArrivals..g.getProductionProcess(k.getId()));
					break;
				case CONTAINER_LOADING:
					loadingProcces.add(loading.getLoading(k.getId()));
					break;
			}
		});
		
//		loading.getLoadingsByPoCode(poCode).forEach(k -> {
//			loadingProcces.addAll(loading.getLoadingsByPoCode(poCode));
//		});
//		ObjectMapper mapper = new ObjectMapper();

	    // create three JSON objects
		Map<String, List<Object>> finalProcesses = new HashMap<String, List<Object>>();
	    finalProcesses.put("orderItemsObj", orders);
	    finalProcesses.put("receiptItemsObj", reciving);
	    finalProcesses.put("testedItemsObj", qcsTested);
//	    finalProcesses.put("transferItemsObj", transferProcces);
	    finalProcesses.put("relocationItemsObj", relocationProcces);
	    finalProcesses.put("cleaningItemsObj", cleaningProcces);
	    finalProcesses.put("roastingItemsObj", roastingProcces);
	    finalProcesses.put("packingItemsObj", packingProcces);
	    finalProcesses.put("arrivalsItemsObj", arrivalProcces);
	    finalProcesses.put("loadingItemsObj", loadingProcces);
		return finalProcesses;
	}
	
	@RequestMapping("/getAllPoCodes")
	public List<PoCodeBasic> getAllPoCodes(){
		return objectTableReader.findAllPoCodes();
	}
	
	@RequestMapping("/getPoFinalReport/{id}")
	public FinalReport getPoFinalReport(@PathVariable("id") int poCode) {
		return processSummaryReader.getFinalReport(poCode);
	}
	
	@RequestMapping("/getFinalSummery/{id}")
	public Map<String, Object> getFinalSummery(@PathVariable("id") int poCode) {
		Map<String, Object> finalProcesses = new HashMap<String, Object>();
	    finalProcesses.put("orders", ordersDao.getOrdersByType(ProcessName.CASHEW_ORDER, new ProcessStatus[]{ProcessStatus.FINAL}, poCode));
	    finalProcesses.put("receiving", receipts.findAllReceiptsByType(new ProcessName[]{ProcessName.CASHEW_RECEIPT}, new ProcessStatus[]{ProcessStatus.FINAL}, poCode));
	    finalProcesses.put("qcRaw", qualityChecks.getRawQualityChecksByPoCode(poCode));
	    
	    List<ProcessRow> rawRelocation = warehouseManagement.getStorageRelocationsByPoCode(poCode, ProductionFunctionality.RAW_STATION);
	    Collections.reverse(rawRelocation);
	    finalProcesses.put("rawRelocation", rawRelocation);
	    
	    List<ProcessRow> cleanRelocation = warehouseManagement.getStorageRelocationsByPoCode(poCode, ProductionFunctionality.ROASTER_IN);
	    Collections.reverse(cleanRelocation);
	    finalProcesses.put("cleanRelocation", cleanRelocation);
	    finalProcesses.put("qcRoast", qualityChecks.getRoastedQualityChecksByPoCode(poCode));
	    finalProcesses.put("cleaning", productionProcesses.getProductionProcessesByTypeAndPoCode(ProcessName.CASHEW_CLEANING, poCode));
	    finalProcesses.put("roasting", productionProcesses.getProductionProcessesByTypeAndPoCode(ProcessName.CASHEW_ROASTING, poCode));
	    finalProcesses.put("packing", productionProcesses.getProductionProcessesByTypeAndPoCode(ProcessName.PACKING, poCode));
//	    finalProcesses.put("arrivals", loading.getLoadingsByPoCode(poCode));
	    finalProcesses.put("loading", loading.getLoadingsByPoCode(poCode));
	    return finalProcesses;
	}
	
	
	
	@RequestMapping("/allProductionByTime/{dateLong}/{type}")
	public List<ProcessRow> allProductionByTime(@PathVariable("dateLong") Long dateLong, @PathVariable("type") String type) {
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateLong), 
                TimeZone.getDefault().toZoneId());
		List<ProcessRow> allProduction = productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_CLEANING);
		allProduction.addAll(productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_ROASTING));
		allProduction.addAll(productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_TOFFEE));
		allProduction.addAll(productionProcesses.getProductionProcessesByType(ProcessName.PACKING));
		switch (type) {
			case "DAY":
				Predicate<ProcessRow> byDate = row -> row.getRecordedTime().getDayOfYear() == date.getDayOfYear() && row.getRecordedTime().getYear() == date.getYear();
				List<ProcessRow> result = allProduction.stream().filter(byDate)
		                .collect(Collectors.toList());
				Collections.sort(result, new Comparator<ProcessRow>() {
					  @Override
					  public int compare(ProcessRow u1, ProcessRow u2) {
					    return u1.getRecordedTime().compareTo(u2.getRecordedTime());
					  }
					});
				return result;
			case "WEEK":
//				OffsetDateTime o = OffsetDateTime.of(date, ZoneOffset.UTC);
//				LocalDate o = date.toLocalDate();
				Predicate<ProcessRow> byDate1 = row -> row.getRecordedTime().isAfter(date) && row.getRecordedTime().isBefore(date.plusDays(8));
				List<ProcessRow> result1 = allProduction.stream().filter(byDate1)
		                .collect(Collectors.toList());
				Collections.sort(result1, new Comparator<ProcessRow>() {
					  @Override
					  public int compare(ProcessRow u1, ProcessRow u2) {
					    return u1.getRecordedTime().compareTo(u2.getRecordedTime());
					  }
					});
				return result1;
			case "MONTH":
				Predicate<ProcessRow> byDate2 = row -> row.getRecordedTime().getMonthValue() == date.getMonthValue() && row.getRecordedTime().getYear() == date.getYear();
				List<ProcessRow> result2 = allProduction.stream().filter(byDate2)
		                .collect(Collectors.toList());
				Collections.sort(result2, new Comparator<ProcessRow>() {
					  @Override
					  public int compare(ProcessRow u1, ProcessRow u2) {
					    return u1.getRecordedTime().compareTo(u2.getRecordedTime());
					  }
					});
				return result2;
			case "YEAR":
				Predicate<ProcessRow> byDate3 = row -> row.getRecordedTime().getYear() == date.getYear();
				List<ProcessRow> result3 = allProduction.stream().filter(byDate3)
		                .collect(Collectors.toList());
				Collections.sort(result3, new Comparator<ProcessRow>() {
					  @Override
					  public int compare(ProcessRow u1, ProcessRow u2) {
					    return u1.getRecordedTime().compareTo(u2.getRecordedTime());
					  }
					});
				return result3;
			default:
				return allProduction;
			}
	}
	
//	@RequestMapping("/getCashewInventoryPacked")
//	public List<ItemInventoryRow> getCashewInventoryPacked() {
//		List<ItemInventoryRow> allInventoryItem = inventoryReports.getInventoryTableByItem(ItemGroup.PRODUCT);
//		Predicate<ItemInventoryRow> byClass = row -> row.getItem().getClazz() == PackedItem.class;
//		List<ItemInventoryRow> result3 = allInventoryItem.stream().filter(byClass)
//                .collect(Collectors.toList());
//		return result3;
//	}
	
	@RequestMapping("/getCashewInventoryPacked")
	public List<ItemInventoryRow> getCashewInventoryPacked() {
		List<ItemInventoryRow> allInventoryItem = inventoryReports.getInventoryTableByItem(ItemGroup.PRODUCT);
		Predicate<ItemInventoryRow> byClass = row -> row.getItem().getUnit().getMeasureUnit() == MeasureUnit.NONE;
		List<ItemInventoryRow> result3 = allInventoryItem.stream().filter(byClass)
		.collect(Collectors.toList());
		return result3;
	}
	
//	@RequestMapping("/getCashewInventoryBullk")
//	public List<ItemInventoryRow> getCashewInventoryBullk() {
//		List<ItemInventoryRow> allInventoryItem = inventoryReports.getInventoryTableByItem(ItemGroup.PRODUCT);
//		Predicate<ItemInventoryRow> byClass = row -> row.getItem().getClazz() == BulkItem.class;
//		List<ItemInventoryRow> result3 = allInventoryItem.stream().filter(byClass)
//                .collect(Collectors.toList());
//		return result3;
//	}
	
	@RequestMapping("/getCashewInventoryBullk")
	public List<ItemInventoryRow> getCashewInventoryBullk() {
		List<ItemInventoryRow> allInventoryItem = inventoryReports.getInventoryTableByItem(ItemGroup.PRODUCT);
		Predicate<ItemInventoryRow> byClass = row -> row.getItem().getUnit().getMeasureUnit() != MeasureUnit.NONE;
		List<ItemInventoryRow> result3 = allInventoryItem.stream().filter(byClass)
	            .collect(Collectors.toList());
		return result3;
	}
	
	@RequestMapping("/getCashewInventoryFinished/{dateLong}")
	public List<FinishedProductInventoryRow> getCashewInventoryFinished(@PathVariable("dateLong") Long dateLong) {
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateLong), 
                TimeZone.getDefault().toZoneId());
		return inventoryReports.getFinishedProductInventoryRows(ItemGroup.PRODUCT, new ProductionUse[] {ProductionUse.PACKED, ProductionUse.ROAST, ProductionUse.TOFFEE}, date);
	}
	
	@RequestMapping("/getCashewInventoryRaw/{dateLong}")
	public List<ReceiptInventoryRow> getCashewInventoryRaw(@PathVariable("dateLong") Long dateLong) {
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateLong), 
                TimeZone.getDefault().toZoneId());
		return inventoryReports.getReceiptInventoryRows(ItemGroup.PRODUCT, new ProductionUse[] {ProductionUse.RAW_KERNEL}, date);
	}
	
	@RequestMapping("/getCashewInventoryBagged/{dateLong}")
	public List<CashewBaggedInventoryRow> getCashewInventoryBagged(@PathVariable("dateLong") Long dateLong) {
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateLong), 
                TimeZone.getDefault().toZoneId());
		return inventoryReports.getCashewBaggedInventoryRows(ItemGroup.PRODUCT, new ProductionUse[] {ProductionUse.PACKED}, date);
	}
	
	@RequestMapping("/getCashewExportReport/{firstLong}/{secondLong}")
	public List<CashewExportReportRow> getCashewExportReport(@PathVariable("firstLong") Long firstLong, @PathVariable("secondLong") Long secondLong) {
		LocalDateTime firstDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(firstLong), 
                TimeZone.getDefault().toZoneId());
		LocalDateTime secondDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(secondLong), 
                TimeZone.getDefault().toZoneId());
		return loadingReports.getCashewExportReportRows(firstDate, secondDate);
	}
	
	@RequestMapping("/getBulkPackCashewItems/{packageType}")
	public List<CashewItemDTO> getBulkPackCashewItems(@PathVariable("packageType") PackageType packageType) {
		return valueTablesReader.getCashewItems(ItemGroup.PRODUCT, null, null, packageType);
	}
	
	
	
	
//	@RequestMapping("/allProductionByRange/{start}/{end}")
//	public List<ProcessRow> allProductionByRange(@PathVariable("start") OffsetDateTime start, @PathVariable("end") OffsetDateTime end) {
//		List<ProcessRow> allProduction = productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_CLEANING);
//		allProduction.addAll(productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_ROASTING));
//		allProduction.addAll(productionProcesses.getProductionProcessesByType(ProcessName.CASHEW_TOFFEE));
//		allProduction.addAll(productionProcesses.getProductionProcessesByType(ProcessName.PACKING));
//		Predicate<ProcessRow> byDate = row -> row.getRecordedTime().getDayOfYear() >= start.getDayOfYear() && row.getRecordedTime().getDayOfYear() <= end.getDayOfYear() &&
//				row.getRecordedTime().getYear() >= start.getYear() && row.getRecordedTime().getYear() <= end.getYear();
//		List<ProcessRow> result = allProduction.stream().filter(byDate)
//                .collect(Collectors.toList());
//		Collections.sort(result, new Comparator<ProcessRow>() {
//			  @Override
//			  public int compare(ProcessRow u1, ProcessRow u2) {
//			    return u1.getRecordedTime().compareTo(u2.getRecordedTime());
//			  }
//			});
//		return result;
//	}
	
//	@RequestMapping("/getPoFinalReport/{id}")
//	@Transactional(readOnly = true)
//	public Map<String, Object> getPoFinalReport(@PathVariable("id") int poCode) {
//		return getFinalReport(poCode);
//	}
	
//	public Map<String, Object> getFinalReport(int poCode) {
//		Map<String, Object> finalProcesses = new HashMap<String, Object>();
//	    finalProcesses.put("receiving", findAllReceiptsByType(poCode));
//	    finalProcesses.put("qcRaw", qualityChecks.getRawQualityChecksByPoCode(poCode));
////	    finalProcesses.put("transferItemsObj", transferProcces);
//	    finalProcesses.put("relocationItems", getStorageRelocationsByPoCode(poCode));
//	    finalProcesses.put("qcRoast", qualityChecks.getRoastedQualityChecksByPoCode(poCode));
//	    finalProcesses.put("cleaning", getProductionProcessesByTypeAndPoCode(ProcessName.CASHEW_CLEANING, poCode));
//	    finalProcesses.put("roasting", getProductionProcessesByTypeAndPoCode(ProcessName.CASHEW_ROASTING, poCode));
//	    finalProcesses.put("packing", getProductionProcessesByTypeAndPoCode(ProcessName.PACKING, poCode));
//	    finalProcesses.put("loading", getLoadingsByPoCode(poCode));
//	    return finalProcesses;
//	}
	
	
//	public List<AmountItemDates> findAllReceiptsByType(Integer poCodeId) {
//		List<ReceiptItemRow> itemRows = getReceiptRepository().findAllReceiptsByType(new ProcessName[] {ProcessName.CASHEW_RECEIPT}, new ProcessStatus[] {ProcessStatus.FINAL}, poCodeId);
//		Map<BasicValueEntity<Item>, List<ReceiptItemRow>> receiptMap = itemRows.stream()
//				.collect(Collectors.groupingBy(ReceiptItemRow::getItem, LinkedHashMap::new, Collectors.toList()));
//		List<AmountItemDates> allItemsWeights = new ArrayList<AmountItemDates>();
//		receiptMap.forEach((k, v) -> {
//			AmountWithUnit totalAmount = v.stream()
//					.map(pi -> pi.getReceiptAmount()[0])
//					.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
//			allItemsWeights.add(new AmountItemDates(k, totalAmount, v.stream().distinct()
//					.map(ReceiptItemRow::getReceiptDate).collect(Collectors.toList())));
//		});
//		if(allItemsWeights.isEmpty()) {
//			return null;
//		}
//		return allItemsWeights;
//	}
//	
//	public List<AmountItemDates> getProductionProcessesByTypeAndPoCode(ProcessName processName, Integer poCodeId) {
//		List<ProcessRow> processRows = getProcessRepository().findProcessByType(processName, poCodeId);
//		int[] processIds = processRows.stream().mapToInt(ProcessRow::getId).toArray();
//		Map<BasicValueEntity<Item>, List<ProductionProcessWithItemAmount>> producedMap = getProcessRepository()
//				.findAllProducedItemsByProcessIds(processIds)
//				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getItem));
//		List<AmountItemDates> allItemsWeights = new ArrayList<AmountItemDates>();
//		producedMap.forEach((k, v) -> {
//			List<OffsetDateTime> allProductionDates = processRows.stream()
//					.filter(c -> v.stream().filter(o -> o.getId().equals(c.getId())).findFirst().isPresent())
//					.map(ProcessRow::getRecordedTime).collect(Collectors.toList());
//			AmountWithUnit totalAmount = v.stream()
//					.map(pi -> pi.getAmountWithUnit()[0])
//					.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
//			allItemsWeights.add(new AmountItemDates(k, totalAmount, allProductionDates));
//		});
//		if(allItemsWeights.isEmpty()) {
//			return null;
//		}
//		return allItemsWeights;
//	}
//	
//	public List<AmountItemDatesUsed> getStorageRelocationsByPoCode(Integer poCodeId) {
//		List<AmountItemDatesUsed> allItemsWeights = new ArrayList<AmountItemDatesUsed>();
//		warehouseManagement.getStorageRelocationsByPoCode(poCodeId).forEach((m) -> {
//			Map<BasicValueEntity<Item>, List<ProductionProcessWithItemAmount>> usedMap = m.getUsedItems().stream()
//					.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getItem));
//			Map<BasicValueEntity<Item>, List<ProductionProcessWithItemAmount>> countMap = new HashMap<BasicValueEntity<Item>, List<ProductionProcessWithItemAmount>>();
//			if(m.getItemCounts() != null) {
//				countMap = m.getItemCounts().stream()
//						.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getItem));
//			}
//			countMap.forEach((k, v) -> {
//				List<ProductionProcessWithItemAmount> newUsed = usedMap.remove(k);
//				AmountWithUnit totalAmount = v.stream()
//						.map(pi -> pi.getAmountWithUnit()[0])
//						.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
//				if(newUsed != null) {
//					AmountWithUnit usedAmount = newUsed.stream()
//							.map(pi -> pi.getAmountWithUnit()[0])
//							.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
//					allItemsWeights.add(new AmountItemDatesUsed(k, totalAmount, m.getRecordedTime(), usedAmount));
//				} else {
//					allItemsWeights.add(new AmountItemDatesUsed(k, totalAmount, m.getRecordedTime(), null));
//				}
//			});
//			usedMap.forEach((k, v) -> {
//				AmountWithUnit usedAmount = v.stream()
//						.map(pi -> pi.getAmountWithUnit()[0])
//						.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
//				allItemsWeights.add(new AmountItemDatesUsed(k, null, m.getRecordedTime(), usedAmount));
//			});
//		});
//		return allItemsWeights;
//	}
//	
//	
//
//	public List<AmountItemDatesSealContinar> getLoadingsByPoCode(@NonNull Integer poCodeId) {
//		List<AmountItemDatesSealContinar> allLoadings = new ArrayList<AmountItemDatesSealContinar>();
//		loading.getLoadingsByPoCode(poCodeId).forEach((m) -> {
//			Map<BasicValueEntity<Item>, List<ProductionProcessWithItemAmount>> usedMap = m.getUsedItems().stream()
//					.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getItem));
//			usedMap.forEach((k, v) -> {
//				AmountWithUnit totalAmount = v.stream()
//						.map(pi -> pi.getAmountWithUnit()[0])
//						.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
//				allLoadings.add(new AmountItemDatesSealContinar(k, totalAmount, m.getRecordedTime(), m.getSealNumber(), m.getContainerNumber()));
//			});
//		});
//		
//		return allLoadings;
//	}
//	
//	
//	@Data
//	private class AmountItemDates
//	{
//		AmountWithUnit[] amounts;
//		BasicValueEntity<Item> item;
//		List<OffsetDateTime> dates;
//
//	    public AmountItemDates(BasicValueEntity<Item> item, AmountWithUnit amounts, List<OffsetDateTime> dates)
//	    {
//	    	if(amounts != null) {
//	    		this.amounts = new AmountWithUnit[2];
//				this.amounts[0] = amounts.setScale(MeasureUnit.SCALE);
//				this.amounts[1] = amounts.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
//	    	}
//	        this.item = item;
//	        this.dates = dates;
//	    }
//	}
//	
//	@Data
//	private class AmountItemDatesSealContinar extends AmountItemDates
//	{
//		String seal;
//		String container;
//		OffsetDateTime date;
//	    public AmountItemDatesSealContinar(BasicValueEntity<Item> item, AmountWithUnit amounts, OffsetDateTime date, String seal, String container)
//	    {
//	    	super(item, amounts, null);
//	    	this.date = date;
//	    	this.seal = seal;
//	    	this.container = container;
//	    }
//	}
//
//	@Data
//	private class AmountItemDatesUsed extends AmountItemDates
//	{
//		OffsetDateTime date;
//		AmountWithUnit[] usedAmounts;
//		AmountWithUnit[] changeGain;
//	    public AmountItemDatesUsed(BasicValueEntity<Item> item, AmountWithUnit amounts, OffsetDateTime date, AmountWithUnit usedAmounts)
//	    {
//	    	super(item, amounts, null);
//	    	if(usedAmounts != null && amounts != null) {
////				AmountWithUnit processGain = amounts.substract(usedAmounts);
////				changeGain = new AmountWithUnit[] {
////						processGain.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE),                        
////						processGain.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE)
////				};
//	    	}
//	    	this.date = date;
//	    	if(usedAmounts != null) {
//	    		this.usedAmounts = new AmountWithUnit[2];
//				this.usedAmounts[0] = usedAmounts.setScale(MeasureUnit.SCALE);
//				this.usedAmounts[1] = usedAmounts.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
//	    	}
//	    }
//	}


}
