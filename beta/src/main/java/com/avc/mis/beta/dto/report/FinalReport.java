/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.util.List;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class FinalReport {
	
	private InventoryReportLine inventory;
	
	private ReceiptReportLine receipt;
	private List<QcReportLine> receiptQC;
	private ProductionReportLine cleaning;
	private ProductionReportLine roasting;
	private List<QcReportLine> roastQC;
	private ProductionReportLine packing;
	private List<LoadingReportLine> loadings;
	

//	@JsonIgnore
//	static AmountWithUnit getTotalWeight(List<ItemAmount> itemAmounts) {
//		return itemAmounts.stream().map(i -> i.getWeight()[0]).reduce(AmountWithUnit::add).get();
//		return new AmountWithUnit[] {
//				totalWeight.convert(MeasureUnit.KG),
//				totalWeight.convert(MeasureUnit.LBS)};
//	}
}
