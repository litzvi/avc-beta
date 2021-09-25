/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.PoProcessDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.collection.OrderItemDTO;
import com.avc.mis.beta.dto.processInfo.GeneralProcessInfo;
import com.avc.mis.beta.dto.processInfo.OrderProcessInfo;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.process.PO;

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
	
	private List<OrderItemDTO> orderItems;
	
	private String personInCharge;
	
	/**
	 * All arguments (besides for order items) Constructor ,
	 * used to project directly from database without nested fetching.
	 */
	public PoDTO(Integer id, Integer version, Instant createdDate, String staffRecording, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName, 
			ProcessName processName, Integer productionLineId, String productionLineValue, ProductionFunctionality productionFunctionality,
			LocalDateTime recordedTime, LocalTime startTime, LocalTime endTime, 
			Duration downtime, Integer numOfWorkers, 
			ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals,
			String personInCharge) {
		super();
		super.setGeneralProcessInfo(new GeneralProcessInfo(id, version, createdDate, staffRecording, 
				processName, productionLineId, productionLineValue, productionFunctionality,
				recordedTime, startTime, endTime, 
				downtime, numOfWorkers, processStatus, editStatus, remarks, approvals));
		super.setPoCode(new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName));
		this.personInCharge = personInCharge;

	}
	
	/**
	 * Constructor from PO object, used for testing.
	 * @param po the PO object
	 */
	public PoDTO(@NonNull PO po) {
		super(po);
		this.personInCharge = po.getPersonInCharge();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toList());

	}
	
	public void setOrderProcessInfo(OrderProcessInfo info) {
		this.personInCharge = info.getPersonInCharge();		
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Purchase Order";
	}
}
