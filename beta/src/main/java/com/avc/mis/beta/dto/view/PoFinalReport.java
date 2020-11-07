/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.enums.ProcessName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoFinalReport extends ValueDTO {
	
	private PoCodeBasic poCode;
	private String supplierName;
	
	//production dates
	private List<ReceiptRow> receipt;
	private List<CashewQcRow> receiptQC;
	private List<ProcessRow> cleaning;
	private List<ProcessRow> relocationCounts;
	private List<ProcessRow> roasting;
	private List<CashewQcRow> roastQC;
	private List<ProcessRow> packing;
	private List<LoadingRow> loading;//add container and seal
	
	public PoFinalReport(@NonNull Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName) {
		super(poCodeId);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.supplierName = supplierName;
	}
	
	public Map<ProcessName, List<OffsetDateTime>> getProductionDates() {
		Map<ProcessName, List<OffsetDateTime>> productionDates = new HashMap<>();
		if(receipt != null) {
			productionDates.put(ProcessName.CASHEW_RECEIPT, 
					receipt.stream().map(i -> i.getReceiptDate()).collect(Collectors.toList()));
		}
		if(receiptQC != null) {
			productionDates.put(ProcessName.CASHEW_RECEIPT_QC, 
					receiptQC.stream().map(i -> i.getCheckDate()).collect(Collectors.toList()));
		}
		if(cleaning != null) {
			productionDates.put(ProcessName.CASHEW_CLEANING, 
					cleaning.stream().map(i -> i.getRecordedTime()).collect(Collectors.toList()));
		}
		if(relocationCounts != null) {
//			productionDates.put(ProcessName.STORAGE_RELOCATION,
			productionDates.put(ProcessName.STORAGE_TRANSFER, 
					relocationCounts.stream().map(i -> i.getRecordedTime()).collect(Collectors.toList()));
		}
		if(roasting != null) {
			productionDates.put(ProcessName.CASHEW_ROASTING, 
					roasting.stream().map(i -> i.getRecordedTime()).collect(Collectors.toList()));
		}
		if(roastQC != null) {
			productionDates.put(ProcessName.ROASTED_CASHEW_QC, 
					roastQC.stream().map(i -> i.getCheckDate()).collect(Collectors.toList()));
		}
		if(packing != null) {
			productionDates.put(ProcessName.PACKING, 
					packing.stream().map(i -> i.getRecordedTime()).collect(Collectors.toList()));
		}
		if(loading != null) {
			productionDates.put(ProcessName.CONTAINER_LOADING, 
					loading.stream().map(i -> i.getRecordedTime()).collect(Collectors.toList()));
		}		
		
		return productionDates;
	}
	

}