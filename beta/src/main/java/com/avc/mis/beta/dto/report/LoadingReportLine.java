/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDateTime;
import java.util.List;

import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.utilities.ListGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class LoadingReportLine extends ProcessStateInfo implements ListGroup<ItemAmount> {

//	@JsonIgnore
//	private Integer processId;
	private ShipmentCodeBasic shipmentCode;
	private ContainerDetails containerDetails;

	private List<ItemAmount> productIn;
	private AmountWithUnit totalProductIn;
	
	public LoadingReportLine(Integer id, 
			Integer shipmentId, String shipmentCode, String portOfDischargeCode, String portOfDischargeValue, 
			ContainerDetails containerDetails, LocalDateTime loadingDate, ProcessStatus status, String approvals) {
		super(id, loadingDate, status, approvals);
//		this.processId = processId;
		this.shipmentCode = new ShipmentCodeBasic(shipmentId, shipmentCode,  portOfDischargeCode, portOfDischargeValue);
		this.containerDetails = containerDetails;
//		super.setProcesses(new ProcessStateInfo(loadingDate.toLocalDate(), status, approvals));
//		super.setDates(Stream.of(loadingDate.toLocalDate()).collect(Collectors.toSet()));
	}

	public void setProductIn(List<ItemAmount> productIn) {
		boolean empty = productIn == null || productIn.isEmpty();
		this.productIn = empty ? null : productIn;
		this.totalProductIn = empty ? null : ItemAmount.getTotalWeight(productIn);
	}

//	@Override
//	public Integer getId() {
//		return getProcessId();
//	}

	@Override
	public void setList(List<ItemAmount> list) {
		setProductIn(list);
	}


}
