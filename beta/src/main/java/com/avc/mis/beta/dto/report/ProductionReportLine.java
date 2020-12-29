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

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.ItemGroup;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class ProductionReportLine {
	
	private List<ItemAmount> productIn;
	private List<ItemAmount> ingredients;
	 
	private List<ItemAmount> productOut;
	private List<ItemAmount> waste;
	private List<ItemAmount> qc;
	 
	private Set<LocalDate> dates;
	 
	private AmountWithUnit totalProductIn;	
	private AmountWithUnit totalIngredients;
	 
	private AmountWithUnit totalProductOut;
	private AmountWithUnit totalWaste;
	private AmountWithUnit totalQC;
	
	public void setProductIn(List<ItemAmount> productIn) {
		this.productIn = productIn;
		this.totalProductIn= getTotalWeight(productIn); 
	}

	public void setIngredients(List<ItemAmount> ingredients) {
		this.ingredients = ingredients;
		this.totalIngredients= getTotalWeight(ingredients); 
	}

	public void setProductOut(List<ItemAmount> productOut) {
		this.productOut = productOut;
		this.totalProductOut = getTotalWeight(productOut); 
	}

	public void setWaste(List<ItemAmount> waste) {
		this.waste = waste;
		this.totalWaste = getTotalWeight(waste); 
	}

	public void setQc(List<ItemAmount> qc) {
		this.qc = qc;
		this.totalQC = getTotalWeight(qc); 
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

	private AmountWithUnit getTotalWeight(List<ItemAmount> itemAmounts) {
		if(itemAmounts == null)
			return null;
		return itemAmounts.stream().map(i -> i.getWeight()[0]).reduce(AmountWithUnit::add).get();
//		return new AmountWithUnit[] {
//				totalWeight.convert(MeasureUnit.KG),
//				totalWeight.convert(MeasureUnit.LBS)};
	}


}
