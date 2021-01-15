/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ItemQc;
import com.avc.mis.beta.dto.report.LoadingReportLine;
import com.avc.mis.beta.dto.report.QcReportLine;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.Data;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
//@Value
//public class ItemQcWithQcReportLine  implements CollectionItemWithGroup<ItemQc, QcReportLine> {
//
//	QcReportLine reportLine;
//	ItemQc itemQc;
//	
//	public ItemQcWithQcReportLine(@NonNull Integer processId, 
//			String checkedBy, 
//			LocalDate date, ProcessStatus status, String approvals, 
//			Integer itemId, String itemValue, 
//			BigDecimal sampleWeight, boolean precentage,
//			BigDecimal humidity, BigDecimal breakage,
//			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour, 
//			BigDecimal shrivel, BigDecimal desert, BigDecimal deepSpot, 
//			BigDecimal mold, BigDecimal dirty, BigDecimal lightDirty, 
//			BigDecimal decay, BigDecimal insectDamage, BigDecimal testa) {
//		this.reportLine = new QcReportLine(processId, checkedBy, date, status, approvals);
//		this.itemQc = new ItemQc(itemId, itemValue, sampleWeight, precentage, 
//				humidity, breakage, scorched, deepCut, offColour, 
//				shrivel, desert, deepSpot, mold, dirty, lightDirty, decay, insectDamage, testa);
//	}
//	
//	
//	@Override
//	public QcReportLine getGroup() {
//		return this.reportLine;
//	}
//
//
//	@Override
//	public ItemQc getItem() {
//		return this.itemQc;
//	}
//	
//}
