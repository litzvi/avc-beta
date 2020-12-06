/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.item.ItemGroup;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class ProductionReportLine {
	
	List<ItemAmount> productIn;
	List<ItemAmount> ingredients;
	
	List<ItemAmount> productOut;
	List<ItemAmount> waste;
	List<ItemAmount> qc;
	
	Set<LocalDate> dates;
	
//	AmountWithUnit[] totalProductIn;	
//	AmountWithUnit[] totalIngredients;	
//	
//	AmountWithUnit[] totalProductOut;
//	AmountWithUnit[] totalWaste;	
//	AmountWithUnit[] totalQC;
	
	public AmountWithUnit[] getDifference() {		
		//TODO
		return null;
	}

}
