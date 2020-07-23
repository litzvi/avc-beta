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

import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.RawItemQualityDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.QualityCheck;
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
public class QualityCheckDTO extends ProductionProcessDTO {

	
	private Set<ProcessItemDTO> processItems; //can use a SortedSet like ContactDetails to maintain order
	private Set<RawItemQualityDTO> testedItems; //can use a SortedSet like ContactDetails to maintain order
	
	public QualityCheckDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks) {
		super(id, version, createdDate, userRecording, 
				poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName, 
				processName, productionLine, recordedTime, duration,
				numOfWorkers, processStatus, editStatus, remarks);
	}
	
	public QualityCheckDTO(@NonNull QualityCheck check) {
		super(check);
		this.processItems = Arrays.stream(check.getProcessItems())
				.map(i->{return new ProcessItemDTO(i);}).collect(Collectors.toSet());
		this.testedItems = Arrays.stream(check.getTestedItems())
				.map(i->{return new RawItemQualityDTO(i);}).collect(Collectors.toSet());
	}
	
	/**
	 * Used for setting processItems from a flat form produced by a join of QC process items and it's storage info, 
	 * to processItems that each have a Set of storages.
	 * @param processItems collection of ProcessItemWithStorage that contains all receipt QC items 
	 * with storage detail.
	 */
	public void setProcessItems(Collection<ProcessItemWithStorage> processItems) {
		Map<Integer, List<ProcessItemWithStorage>> map = processItems.stream()
			.collect(Collectors.groupingBy(ProcessItemWithStorage::getId, Collectors.toList()));
		this.processItems = new HashSet<>();
		for(List<ProcessItemWithStorage> list: map.values()) {
			ProcessItemDTO processItem = list.get(0).getProcessItem();
			processItem.setStorageForms(list.stream().map(i -> i.getStorage()).collect(Collectors.toSet()));
			this.processItems.add(processItem);
		}
		
	}

	@Override
	public String getProcessTypeDescription() {
		return "Quality Check";
	}
	

	
//	/**
//	 * Used for setting checkItems from a flat form produced by a join of QC items and it's storage info, 
//	 * to checkItems that each have a Set of storages.
//	 * @param checkItems collection of RawItemQualityWithStorage that contains all receipt QC items 
//	 * with storage detail.
//	 */
//	public void setCheckItems(Collection<RawItemQualityWithStorage> checkItems) {
//		Map<Integer, List<RawItemQualityWithStorage>> map = checkItems.stream()
//			.collect(Collectors.groupingBy(RawItemQualityWithStorage::getId, Collectors.toList()));
//		this.checkItems = new HashSet<>();
//		for(List<RawItemQualityWithStorage> list: map.values()) {
//			RawItemQualityDTO checkItem = list.get(0).getRawItemQuality();
//			checkItem.setStorageForms(list.stream().map(i -> i.getStorage()).collect(Collectors.toSet()));
//			this.checkItems.add(checkItem);
//		}
//		
//	}

	
}
