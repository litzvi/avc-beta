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
public class ProductReportLine extends ReportLine {

//	public ProductReportLine(@NonNull Integer id) {
//		super(id);
//	}

	private List<ItemAmount> productCount;

	private AmountWithUnit totalProductCount;	
	
	public void setProductCount(List<ItemAmount> productCount) {
		boolean empty = productCount == null || productCount.isEmpty();
		this.productCount = empty ? null : productCount;
		this.totalProductCount = empty ? null : getTotalWeight(productCount);
	}
	


	static AmountWithUnit getTotalWeight(List<ItemAmount> itemAmounts) {
		return itemAmounts.stream().map(i -> i.getWeight()[0]).reduce(AmountWithUnit::add).get();
//		return new AmountWithUnit[] {
//				totalWeight.convert(MeasureUnit.KG),
//				totalWeight.convert(MeasureUnit.LBS)};
	}


}
