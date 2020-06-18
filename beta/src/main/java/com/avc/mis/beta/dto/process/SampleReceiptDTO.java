/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;

import com.avc.mis.beta.dto.queryRows.SampleItemWithWeight;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.RecordStatus;
import com.avc.mis.beta.entities.process.SampleReceipt;
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
public class SampleReceiptDTO extends ProductionProcessDTO {

	//not set because we can have doubles, order should be unimportant for testing - so bag needed
	private MultiSet<SampleItemDTO> sampleItems; 	
	
	public SampleReceiptDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, ContractTypeCode contractTypeCode, Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, RecordStatus status, String remarks) {
		super(id, version, createdDate, userRecording, 
				poCodeId, contractTypeCode, supplierId, supplierVersion, supplierName, 
				processName, productionLine, recordedTime, duration,
				numOfWorkers, status, remarks);
	}
	
	public SampleReceiptDTO(@NonNull SampleReceipt sample) {
		super(sample);
		this.sampleItems = Arrays.stream(sample.getSampleItems())
				.map(i->{return new SampleItemDTO(i);})
				.collect(Collectors.toCollection(() -> {return new HashMultiSet<SampleItemDTO>();}));
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

}
