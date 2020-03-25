/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.data.Staff;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PoDTO extends ProcessDTO {
	
	private ContractType contractType;
	private SupplierBasic supplier;
	private OrderStatus status;
	private ProductionProcessDTO orderProcess;
	private List<OrderItemDTO> orderItems;
	
	public PoDTO(Integer id, Long version, ContractType contractType, Integer supplierId, 
			Long supplierVersion, String supplierName, OrderStatus status, 
			Integer processId, Long processVersion, Instant createdDate, Staff staffRecording, ProcessType processType,
			ProductionLine productionLine, LocalDateTime time, Duration duration, Integer numOfWorkers, 
			ProcessStatus processStatus, String remarks) {
		super(id, version);
		this.orderProcess = new ProductionProcessDTO(processId, processVersion, createdDate, staffRecording, id, 
				processType, productionLine, time, duration, numOfWorkers, processStatus, remarks);
		this.contractType = contractType;
		this.supplier = new SupplierBasic(supplierId, supplierVersion, supplierName);
		this.status = status;
//		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
	
	public PoDTO(@NonNull PO po) {
		super(po.getId(), po.getVersion());
		this.orderProcess = new ProductionProcessDTO(po.getOrderProcess());
		this.contractType = po.getContractType();
		this.supplier = new SupplierBasic(po.getSupplier());
		this.status = po.getStatus();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toList());

	}
}
