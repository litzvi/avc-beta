/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.util.List;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Receipt report summary for final report.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ReceiptReportLine extends ProductReportLine {

	private List<ItemAmount> received;

	private AmountWithUnit totalReceived;


	public void setReceived(List<ItemAmount> received) {
		boolean empty = received == null || received.isEmpty();
		this.received = empty ? null : received;
		this.totalReceived = empty ? null : ItemAmount.getTotalWeight(received);
	}

}
