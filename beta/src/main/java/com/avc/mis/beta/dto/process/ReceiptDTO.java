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

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;

import com.avc.mis.beta.dto.processinfo.ItemWeightDTO;
import com.avc.mis.beta.dto.processinfo.ReceiptItemDTO;
import com.avc.mis.beta.dto.processinfo.SampleItemDTO;
import com.avc.mis.beta.dto.query.ReceiptItemWithStorage;
import com.avc.mis.beta.dto.query.SampleItemWithWeight;
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
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ReceiptDTO extends ProductionProcessDTO {

	private Set<ReceiptItemDTO> receiptItems; //can use a SortedSet like ContactDetails to maintain order
	
	//not set because we can have doubles, order should be unimportant for testing - so bag needed
	private MultiSet<SampleItemDTO> sampleItems; 	
		
	public ReceiptDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks) {
		super(id, version, createdDate, userRecording, 
				poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName, 
				processName, productionLine, recordedTime, duration,
				numOfWorkers, processStatus, editStatus, remarks);
	}

	/**
	 * @param process
	 */
	public ReceiptDTO(@NonNull Receipt receipt) {
		super(receipt);
		this.receiptItems = Arrays.stream(receipt.getProcessItems())
				.map(i->{return new ReceiptItemDTO((ReceiptItem) i);}).collect(Collectors.toSet());
		this.sampleItems = Arrays.stream(receipt.getSampleItems())
				.map(i->{return new SampleItemDTO(i);})
				.collect(Collectors.toCollection(() -> {return new HashMultiSet<SampleItemDTO>();}));
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
	
	public void setSampleItems(Collection<SampleItemDTO> sampleItems) {
		this.sampleItems = new HashMultiSet<SampleItemDTO>(sampleItems);
	}
	
	public void setSampleItems(List<SampleItemWithWeight> sampleItems) {
		Map<Integer, List<SampleItemWithWeight>> map = sampleItems.stream()
				.collect(Collectors.groupingBy(SampleItemWithWeight::getId, Collectors.toList()));
			this.sampleItems = new HashMultiSet<>();
			for(List<SampleItemWithWeight> list: map.values()) {
				SampleItemDTO sampleItem = list.get(0).getSampleItem();
				sampleItem.setItemWeights(list.stream().map(i -> i.getItemWeight())
						.collect(Collectors.toCollection(() -> {return new HashMultiSet<ItemWeightDTO>();})));
				this.sampleItems.add(sampleItem);
			}
	}
	

	@Override
	public String getProcessTypeDescription() {
		return "Receipt";
	}

}
