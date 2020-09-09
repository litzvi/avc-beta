/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.RawItemQualityDTO;
import com.avc.mis.beta.dto.processinfo.StorageDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.entities.Ordinal;
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
 * DTO(Data Access Object) for sending or displaying QualityCheck entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class QualityCheckDTO extends PoProcessDTO {

	private String inspector;
	private String sampleTaker;
		
	private Set<ProcessItemDTO> processItems; //can use a SortedSet like ContactDetails to maintain order
	private Set<RawItemQualityDTO> testedItems; //can use a SortedSet like ContactDetails to maintain order
	
	public QualityCheckDTO(Integer id, Integer version, String inspector, String sampleTaker,
			Instant createdDate, String userRecording, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, 
				poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName, 
				processName, productionLine, recordedTime, startTime, endTime, duration,
				numOfWorkers, processStatus, editStatus, remarks, approvals);
		this.inspector = inspector;
		this.sampleTaker = sampleTaker;
	}
	
	public QualityCheckDTO(@NonNull QualityCheck check) {
		super(check);
		this.inspector = check.getInspector();
		this.sampleTaker = check.getSampleTaker();
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
			processItem.setStorageForms(list.stream().map(i -> i.getStorage())
					.collect(Collectors.toCollection(() -> new TreeSet<StorageDTO>(Ordinal.ordinalComparator()))));
			this.processItems.add(processItem);
		}
		
	}

	@Override
	public String getProcessTypeDescription() {
		return "Quality Check";
	}
	
}
