/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ReceiptItemDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying Receipt entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ReceiptDTO extends ProcessWithProductDTO<ReceiptItemDTO> {
	
	@EqualsAndHashCode.Exclude
	private Integer referencedOrder;
	
	public ReceiptDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName, 
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, 
				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName, 
				processName, productionLine, recordedTime, startTime, endTime, duration,
				numOfWorkers, processStatus, editStatus, remarks, approvals);
	}

	public ReceiptDTO(@NonNull Receipt receipt) {
		super(receipt);
		setReceiptItems(Arrays.stream(receipt.getReceiptItems())
				.map(i->{return new ReceiptItemDTO((ReceiptItem) i);}).collect(Collectors.toList()));

	}
	
	public List<ReceiptItemDTO> getReceiptItems() {
		return super.getProcessItems();
	}
	
	public void setReceiptItems(List<ReceiptItemDTO> receiptItems) {
		super.setProcessItems(receiptItems);
		this.referencedOrder = receiptItems.stream().findAny().map(i -> i.getReferencedOrder()).orElse(null);
	}

	
	/**
	 * Used for setting receiptItems from a flat form produced by a join of receipt items and it's storage info, 
	 * to receiptItems that each have a Set of storages.
	 * @param receiptItemsWithStorage collection of ReceiptItemWithStorage that contain all receipt items with storage detail.
	 */
//	public void setReceiptItemsWithStorage(List<ReceiptItemWithStorage> receiptItemsWithStorage) {
//		Map<Integer, List<ReceiptItemWithStorage>> map = receiptItemsWithStorage.stream()
//			.collect(Collectors.groupingBy(ReceiptItemWithStorage::getId, LinkedHashMap::new, Collectors.toList()));
//		List<ReceiptItemDTO> receiptItems = new ArrayList<ReceiptItemDTO>();
//		for(List<ReceiptItemWithStorage> list: map.values()) {
//			ReceiptItemDTO receiptItem = list.get(0).getReceiptItem();
//			//group list to storage/extraAdded and set accordingly
//			receiptItem.setStorageForms(list.stream()
//					.map(i -> i.getStorage())
////					.sorted(Ordinal.ordinalComparator())
//					.collect(Collectors.toList()));
//			receiptItems.add(receiptItem);
//		}
//		setReceiptItems(receiptItems);
////		this.receiptItems.sort(Ordinal.ordinalComparator());
//	}	

	@Override
	public String getProcessTypeDescription() {
		return "Receipt";
	}

	

}
