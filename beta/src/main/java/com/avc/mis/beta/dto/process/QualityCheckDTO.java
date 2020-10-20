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

import com.avc.mis.beta.dto.processinfo.CashewItemQualityDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
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

	private String checkedBy;
	
	private String inspector;
	private String sampleTaker;
		
	private List<ProcessItemDTO> processItems; //can use a SortedSet like ContactDetails to maintain order
	private List<CashewItemQualityDTO> testedItems; //can use a SortedSet like ContactDetails to maintain order
	
	public QualityCheckDTO(Integer id, Integer version, String inspector, String sampleTaker, String checkedBy,
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
		this.checkedBy = checkedBy;
		this.inspector = inspector;
		this.sampleTaker = sampleTaker;
	}
	
	public QualityCheckDTO(@NonNull QualityCheck check) {
		super(check);
		this.checkedBy = check.getCheckedBy();
		this.inspector = check.getInspector();
		this.sampleTaker = check.getSampleTaker();
		this.processItems = Arrays.stream(check.getProcessItems())
				.map(i->{return new ProcessItemDTO(i);}).collect(Collectors.toList());
		this.testedItems = Arrays.stream(check.getTestedItems())
				.map(i->{return new CashewItemQualityDTO(i);}).collect(Collectors.toList());
	}
	
	/**
	 * Used for setting processItems from a flat form produced by a join of QC process items and it's storage info, 
	 * to processItems that each have a Set of storages.
	 * @param processItems collection of ProcessItemWithStorage that contains all receipt QC items 
	 * with storage detail.
	 */
	public void setProcessItems(List<ProcessItemWithStorage> processItems) {
		Map<Integer, List<ProcessItemWithStorage>> map = processItems.stream()
			.collect(Collectors.groupingBy(ProcessItemWithStorage::getId, Collectors.toList()));
		this.processItems = new ArrayList<ProcessItemDTO>();
		for(List<ProcessItemWithStorage> list: map.values()) {
			ProcessItemDTO processItem = list.get(0).getProcessItem();
			processItem.setStorageForms(list.stream()
					.map(i -> i.getStorage())
					.sorted(Ordinal.ordinalComparator())
					.collect(Collectors.toList()));
			this.processItems.add(processItem);
		}
		this.processItems.sort(Ordinal.ordinalComparator());
	}
	
	
	@Override
	public String getProcessTypeDescription() {
		return "Quality Check";
	}
	
}
