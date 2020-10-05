/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.SampleItemDTO;
import com.avc.mis.beta.dto.query.SampleItemWithWeight;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
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
public class SampleReceiptDTO extends PoProcessDTO {

	//not set because we can have doubles, order should be unimportant for testing - so bag needed
	private List<SampleItemDTO> sampleItems; 	
	
	public SampleReceiptDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, 
				poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName, 
				processName, productionLine, recordedTime, startTime, endTime, duration,
				numOfWorkers, processStatus, editStatus, remarks, approvals);
	}
	
	public SampleReceiptDTO(@NonNull SampleReceipt sample) {
		super(sample);
		this.sampleItems = Arrays.stream(sample.getSampleItems())
				.map(i->{return new SampleItemDTO(i);})
				.collect(Collectors.toList());
	}
	
//	public void setSampleItems(Collection<SampleItemDTO> sampleItems) {
//		this.sampleItems = new HashMultiSet<SampleItemDTO>(sampleItems);
//	}
	
	public void setSampleItems(List<SampleItemWithWeight> sampleItems) {
		Map<Integer, List<SampleItemWithWeight>> map = sampleItems.stream()
				.collect(Collectors.groupingBy(SampleItemWithWeight::getId, Collectors.toList()));
			this.sampleItems = new ArrayList<SampleItemDTO>();
			for(List<SampleItemWithWeight> list: map.values()) {
				SampleItemDTO sampleItem = list.get(0).getSampleItem();
				sampleItem.setItemWeights(list.stream()
						.map(i -> i.getItemWeight())
						.sorted(Ordinal.ordinalComparator())
						.collect(Collectors.toList()));
				this.sampleItems.add(sampleItem);
			}
			this.sampleItems.sort(Ordinal.ordinalComparator());
	}

	@Override
	public String getProcessTypeDescription() {
		return "Sample Cashew Receipt";
	}

}
