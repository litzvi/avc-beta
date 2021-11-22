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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Loading summary details for final report.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class LoadingReportLine extends ProcessStateInfo {

	private ShipmentCodeBasic shipmentCode;
	private ContainerDetails containerDetails;

	private List<ItemAmount> productIn;
	private AmountWithUnit totalProductIn;
	
	public LoadingReportLine(Integer id, 
			Integer shipmentId, String shipmentCode, String portOfDischargeCode, String portOfDischargeValue, 
			ContainerDetails containerDetails, LocalDateTime loadingDate, ProcessStatus status, String approvals) {
		super(id, loadingDate, status, approvals);
		this.shipmentCode = new ShipmentCodeBasic(shipmentId, shipmentCode,  portOfDischargeCode, portOfDischargeValue);
		this.containerDetails = containerDetails;
	}

	public void setProductIn(List<ItemAmount> productIn) {
		boolean empty = productIn == null || productIn.isEmpty();
		this.productIn = empty ? null : productIn;
		this.totalProductIn = empty ? null : ItemAmount.getTotalWeight(productIn);
	}

}
