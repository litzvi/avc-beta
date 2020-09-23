/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.OrderItemDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Data transfer object for PO class, 
 * used to query database using select constructor (projection)
 * and for presenting relevant information to user.
 * Contains all fields of PO class needed by user for creation or presentation.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class PoDTO extends PoProcessDTO {
	
	private Set<OrderItemDTO> orderItems; //can use a SortedSet like ContactDetails to maintain order
	
	private String personInCharge;
	
	/**
	 * All arguments (besides for order items) Constructor ,
	 * used to project directly from database without nested fetching.
	 */
	public PoDTO(Integer id, Integer version, Instant createdDate, String staffRecording, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, 
			Duration duration, Integer numOfWorkers, 
			ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals,
			String personInCharge) {
		super(id, version, createdDate, staffRecording, 
				poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName, 
				processName, productionLine, 
				recordedTime, startTime, endTime, 
				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
		this.personInCharge = personInCharge;

	}
	
	/**
	 * Constructor from PO object, used for testing.
	 * @param po the PO object
	 */
	public PoDTO(@NonNull PO po) {
		super(po);
		this.personInCharge = po.getPersonInCharge();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Purchase Order";
	}
}
