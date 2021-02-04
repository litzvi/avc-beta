/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

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
	
	public List<ProductPercentage> getProductPercentageLoss() {
		List<ProductPercentage> productPercentageLoss = new ArrayList<>();
		if(receipt == null) {
			return null;
		}
		AmountWithUnit receivedOrderUnits = receipt.getReceivedOrderUnits();
		AmountWithUnit totalReceiptCount = receipt.getTotalProductCount();
		
		ProductPercentage productPercentage;
		if(cleaning != null) {			
			productPercentage = new ProductPercentage("cleaning",
					AmountWithUnit.percentageLoss(cleaning.getTotalProductOut(), receivedOrderUnits),
					AmountWithUnit.percentageLoss(cleaning.getTotalProductOut(), totalReceiptCount));
			productPercentageLoss.add(productPercentage);
		}
		if(roasting != null) {			
			productPercentage = new ProductPercentage("roasting",
					AmountWithUnit.percentageLoss(roasting.getTotalProductOut(), receivedOrderUnits),
					AmountWithUnit.percentageLoss(roasting.getTotalProductOut(), totalReceiptCount));
			productPercentageLoss.add(productPercentage);
		}
		if(packing != null) {			
			productPercentage = new ProductPercentage("packing",
					AmountWithUnit.percentageLoss(packing.getTotalProductOut(), receivedOrderUnits),
					AmountWithUnit.percentageLoss(packing.getTotalProductOut(), totalReceiptCount));
			productPercentageLoss.add(productPercentage);
		}
		
		return productPercentageLoss;
	}
}
