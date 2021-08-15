/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.time.Instant;
import java.util.stream.Stream;

import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessName;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class TaskRow {

	String[] poCodes;
	String[] suppliers;
//	String title;
	Integer processId;
	ProcessName processName;
	String processType;
	Instant createdDate;
	Instant modifiedDate;
	String modifiedBy;
	DecisionType decisionType;
	String processSnapshot;
	
	
	public TaskRow(String poCodes, String suppliers, Integer processId, ProcessName processName, String processType,
			Instant createdDate, Instant modifiedDate, String modifiedBy, DecisionType decisionType, String processSnapshot) {
		super();
//		this.poCodes = poCodes;
//		this.suppliers = suppliers;
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).distinct().toArray(String[]::new);
		else
			this.poCodes = null;
		if(suppliers != null)
			this.suppliers = Stream.of(suppliers.split(",")).distinct().toArray(String[]::new);
		else
			this.suppliers = null;
		this.processId = processId;
		this.processName = processName;
		this.processType = processType;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
		if(decisionType != null)
			this.decisionType = decisionType;
		else
			this.decisionType = DecisionType.NOT_ATTENDED;
		this.processSnapshot = processSnapshot;
	}
	
	public String getTitle() {
		if(this.decisionType == DecisionType.NOT_ATTENDED) {
			return String.format("%s %s", "Added", this.processType); 
		}
		else {
			return String.format("%s %s", "Edited", this.processType);
		}
	}
	
	
}
