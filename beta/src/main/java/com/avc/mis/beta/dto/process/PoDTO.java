/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.collectionItems.OrderItemDTO;
import com.avc.mis.beta.dto.process.info.GeneralProcessInfo;
import com.avc.mis.beta.dto.process.info.OrderProcessInfo;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.enums.Shift;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.collectionItems.OrderItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
	
	private boolean closed;
	
	/**
	 * All arguments (besides for order items) Constructor ,
	 * used to project directly from database without nested fetching.
	 */
	public PoDTO(Integer id, Integer version, Instant createdDate, String staffRecording, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName, 
			ProcessName processName, Integer productionLineId, String productionLineValue, ProductionFunctionality productionFunctionality,
			LocalDateTime recordedTime, Shift shift, LocalTime startTime, LocalTime endTime, 
			Duration downtime, Integer numOfWorkers, String personInCharge, 
			ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals,
			boolean closed) {
		super();
		super.setGeneralProcessInfo(new GeneralProcessInfo(id, version, createdDate, staffRecording, 
				processName, productionLineId, productionLineValue, productionFunctionality,
				recordedTime, shift, startTime, endTime, 
				downtime, numOfWorkers, personInCharge, processStatus, editStatus, remarks, approvals));
		super.setPoCode(new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName));
		this.closed = closed;
	}
	
	public void setOrderProcessInfo(OrderProcessInfo info) {
		this.closed = info.isClosed();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return PO.class;
	}
	
	@Override
	public PO fillEntity(Object entity) {
		PO order;
		if(entity instanceof PO) {
			order = (PO) entity;
		}
		else {
			throw new IllegalStateException("Param has to be PO class");
		}
		super.fillEntity(order);
				
		if(getOrderItems() == null || getOrderItems().isEmpty()) {
			throw new IllegalArgumentException("Purchase Order has to have at least one order line");
		}
		else {
			Ordinal.setOrdinals(getOrderItems());
			order.setOrderItems(getOrderItems().stream().map(i -> i.fillEntity(new OrderItem())).collect(Collectors.toSet()));
		}
		
		return order;
	}

}
