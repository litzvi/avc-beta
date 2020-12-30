/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.ItemGroup;

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
public class ProductionReportLine extends ReportLine {
	
	private List<ItemAmount> productIn;
	private List<ItemAmount> ingredients;
	 
	private List<ItemAmount> productOut;
	private List<ItemAmount> waste;
	private List<ItemAmount> qc;

//	private Set<LocalDate> dates;
//	private List<ItemAmount> productCount;
	 
	private AmountWithUnit totalProductIn;	
	private AmountWithUnit totalIngredients;
	 
	private AmountWithUnit totalProductOut;
	private AmountWithUnit totalWaste;
	private AmountWithUnit totalQC;
	
//	private AmountWithUnit totalProductCount;	
	
	public void setProductIn(List<ItemAmount> productIn) {
		boolean empty = productIn == null || productIn.isEmpty();
		this.productIn = empty ? null : productIn;
		this.totalProductIn = empty ? null : getTotalWeight(productIn);
	}

	public void setIngredients(List<ItemAmount> ingredients) {
		boolean empty = ingredients == null || ingredients.isEmpty();
		this.ingredients = empty ? null : ingredients;
		this.totalIngredients = empty ? null : getTotalWeight(ingredients);
	}

	public void setProductOut(List<ItemAmount> productOut) {
		boolean empty = productOut == null || productOut.isEmpty();
		this.productOut = empty ? null : productOut;
		this.totalProductOut = empty ? null : getTotalWeight(productOut);
	}

	public void setWaste(List<ItemAmount> waste) {
		boolean empty = waste == null || waste.isEmpty();
		this.waste = empty ? null : waste;
		this.totalWaste = empty ? null : getTotalWeight(waste);
	}

	public void setQc(List<ItemAmount> qc) {
		boolean empty = qc == null || qc.isEmpty();
		this.qc = empty ? null : qc;
		this.totalQC = empty ? null : getTotalWeight(qc);
	}

	public AmountWithUnit getDifference() {		
		AmountWithUnit totalOut = AmountWithUnit.addNullable(AmountWithUnit.addNullable(totalProductOut, totalWaste), totalQC);
		AmountWithUnit totalIn = AmountWithUnit.addNullable(totalProductIn, totalIngredients);
		
		return AmountWithUnit.subtractNullable(totalOut, totalIn);
//		if(totalWaste.isPresent()) {
//			calculate(difference, totalWaste, (AmountWithUnit::add));
//		}
		
//		return totalProductOut.orElse(AmountWithUnit.ZERO_KG)
//				.add(totalWaste.orElse(AmountWithUnit.ZERO_KG))
//				.add(totalQC.orElse(AmountWithUnit.ZERO_KG))
//				.subtract(totalProductIn.orElse(AmountWithUnit.ZERO_KG))
//				.subtract(totalIngredients.orElse(AmountWithUnit.ZERO_KG));
	}


}
