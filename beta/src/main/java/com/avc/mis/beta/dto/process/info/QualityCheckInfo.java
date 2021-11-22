/**
 * 
 */
package com.avc.mis.beta.dto.process.info;

import com.avc.mis.beta.entities.enums.QcCompany;

import lombok.Value;

/**
 * For fetching QualityCheckDTO fields.
 * 
 * @author zvi
 *
 */
@Value
public class QualityCheckInfo {

	String checkedBy;	
	String inspector;
	String sampleTaker;
	
	
	public QualityCheckInfo(QcCompany checkedBy, String inspector, String sampleTaker) {
		super();
		this.checkedBy = checkedBy.toString();
		this.inspector = inspector;
		this.sampleTaker = sampleTaker;
	}
	
	

}
