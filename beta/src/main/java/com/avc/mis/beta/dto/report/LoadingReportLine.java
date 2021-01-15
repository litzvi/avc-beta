/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
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
public class LoadingReportLine extends ReportLine implements ListGroup<ItemAmount> {

	private ShipmentCodeBasic shipmentCode;
	private ContainerDetails containerDetails;

	private List<ItemAmount> productIn;
	private AmountWithUnit totalProductIn;
	
	public LoadingReportLine(
			Integer id, String code, String portOfDischargeCode, String portOfDischargeValue, 
			ContainerDetails containerDetails, OffsetDateTime loadingDate) {
		super();
		this.shipmentCode = new ShipmentCodeBasic(id, code, portOfDischargeCode, portOfDischargeValue);
		this.containerDetails = containerDetails;
		super.setDates(Stream.of(loadingDate.toLocalDate()).collect(Collectors.toSet()));
	}

	public void setProductIn(List<ItemAmount> productIn) {
		boolean empty = productIn == null || productIn.isEmpty();
		this.productIn = empty ? null : productIn;
		this.totalProductIn = empty ? null : getTotalWeight(productIn);
	}

	@Override
	public Integer getId() {
		return shipmentCode.getId();
	}

	@Override
	public void setList(List<ItemAmount> list) {
		setProductIn(list);
	}


}
