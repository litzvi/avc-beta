/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class ReportLine {

	private Set<LocalDate> dates;
	private List<ItemAmount> productCount;

	private AmountWithUnit totalProductCount;	
	
	public void setProductCount(List<ItemAmount> productCount) {
		boolean empty = productCount == null || productCount.isEmpty();
		this.productCount = empty ? null : productCount;
		this.totalProductCount = empty ? null : getTotalWeight(productCount);
	}


	AmountWithUnit getTotalWeight(List<ItemAmount> itemAmounts) {
		return itemAmounts.stream().map(i -> i.getWeight()[0]).reduce(AmountWithUnit::add).get();
//		return new AmountWithUnit[] {
//				totalWeight.convert(MeasureUnit.KG),
//				totalWeight.convert(MeasureUnit.LBS)};
	}


}
