/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.data.Staff;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.ContractType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProcessStatus;
import com.avc.mis.beta.entities.process.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class PoDTO implements Serializable {
	
	@EqualsAndHashCode.Exclude
	private Integer id;
	private ContractType contractType;
	private SupplierBasic supplier;
	private OrderStatus status;
	private ProductionProcessDTO orderProcess;
	private List<OrderItemDTO> orderItems;
	
	public PoDTO(Integer id, ContractType contractType, Integer supplierId, 
			String supplierName, OrderStatus status, 
			Integer processId, Instant insertTime, Staff staffRecording, ProcessType processType,
			ProductionLine productionLine, LocalDateTime time, Duration duration, Integer numOfWorkers, 
			ProcessStatus processStatus, String remarks) {
		this.id = id;
		this.orderProcess = new ProductionProcessDTO(processId, insertTime, staffRecording, id, 
				processType, productionLine, time, duration, numOfWorkers, processStatus, remarks);
		this.contractType = contractType;
		this.supplier = new SupplierBasic(supplierId, supplierName);
		this.status = status;
//		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
	
	public PoDTO(@NonNull PO po) {
		this.id = po.getId();
		this.orderProcess = new ProductionProcessDTO(po.getOrderProcess());
		this.contractType = po.getContractType();
		this.supplier = new SupplierBasic(po.getSupplier());
		this.status = po.getStatus();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toList());

	}
}
