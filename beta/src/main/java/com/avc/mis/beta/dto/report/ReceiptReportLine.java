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
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ReceiptReportLine extends ProductReportLine {

//	public ReceiptReportLine(@NonNull Integer id) {
//		super(id);
//	}
	
//	private PoCodeBasic poCode;
//	private String supplierName;
	
//	private AmountWithUnit receivedOrderUnits; //if used, should be a list

	private List<ItemAmount> received;

	private AmountWithUnit totalReceived;


	public void setReceived(List<ItemAmount> received) {
		boolean empty = received == null || received.isEmpty();
		this.received = empty ? null : received;
		this.totalReceived = empty ? null : ItemAmount.getTotalWeight(received);
//		
//		if(received == null || received.isEmpty()) {
//			this.received = null;
//			this.totalReceived = null;
//		}
//		else {
//			this.received = received;
//			this.totalReceived = getTotalWeight(received);
//		}
	}

}
