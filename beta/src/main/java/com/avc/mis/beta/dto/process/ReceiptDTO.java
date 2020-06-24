/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ReceiptItemDTO;
import com.avc.mis.beta.dto.queryRows.ReceiptItemWithStorage;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.RecordStatus;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ReceiptDTO extends ProductionProcessDTO {

	private Set<ReceiptItemDTO> receiptItems; //can use a SortedSet like ContactDetails to maintain order
	
	public ReceiptDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, ContractTypeCode contractTypeCode, Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, RecordStatus status, String remarks) {
		super(id, version, createdDate, userRecording, 
				poCodeId, contractTypeCode, supplierId, supplierVersion, supplierName, 
				processName, productionLine, recordedTime, duration,
				numOfWorkers, status, remarks);
	}

	/**
	 * @param process
	 */
	public ReceiptDTO(@NonNull Receipt receipt) {
		super(receipt);
		this.receiptItems = Arrays.stream(receipt.getProcessItems())
				.map(i->{return new ReceiptItemDTO((ReceiptItem) i);}).collect(Collectors.toSet());

	}
	
	/**
	 * Used for setting receiptItems from a flat form produced by a join of receipt items and it's storage info, 
	 * to receiptItems that each have a Set of storages.
	 * @param receiptItems collection of ReceiptItemWithStorage that contain all receipt items with storage detail.
	 */
	public void setReceiptItems(Collection<ReceiptItemWithStorage> receiptItems) {
		Map<Integer, List<ReceiptItemWithStorage>> map = receiptItems.stream()
			.collect(Collectors.groupingBy(ReceiptItemWithStorage::getId, Collectors.toList()));
		this.receiptItems = new HashSet<>();
		for(List<ReceiptItemWithStorage> list: map.values()) {
			ReceiptItemDTO receiptItem = list.get(0).getReceiptItem();
			//group list to storage/extraAdded and set accordingly
			receiptItem.setStorageForms(list.stream().map(i -> i.getStorage()).collect(Collectors.toSet()));
			this.receiptItems.add(receiptItem);
		}
		
	}
	
	

}
