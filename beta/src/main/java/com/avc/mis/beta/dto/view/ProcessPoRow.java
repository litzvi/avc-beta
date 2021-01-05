/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.util.List;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

/**
 * @author zvi
 *
 */
public class ProcessPoRow extends BasicDTO {

	String processType;
	String[] dates;
	
	List<ProductionProcessWithItemAmount> usedItems;
	List<ProductionProcessWithItemAmount> producedItems;
	List<ProductionProcessWithItemAmount> wasteItems;	
	
	public AmountWithUnit[] getTotalUsed() {
		//TODO
		return null;
	}
	
	public AmountWithUnit[] getTotalProduced() {
		//TODO
		return null;
	}
	
	public AmountWithUnit[] getTotalWaste() {
		//TODO
		return null;
	}
	
	public AmountWithUnit[] getTotalGain() {
		//TODO
		return null;
	}
	
}
