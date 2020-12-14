/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	
	List<ItemAmount> productIn;
	List<ItemAmount> ingredients;
	
	List<ItemAmount> productOut;
	List<ItemAmount> waste;
	List<ItemAmount> qc;
	
	Set<LocalDate> dates;
	
	Optional<AmountWithUnit> totalProductIn = Optional.empty();	
	Optional<AmountWithUnit> totalIngredients = Optional.empty();	
	
	Optional<AmountWithUnit> totalProductOut = Optional.empty();
	Optional<AmountWithUnit> totalWaste = Optional.empty();
	Optional<AmountWithUnit> totalQC = Optional.empty();
	
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
		return totalProductOut.orElse(AmountWithUnit.ZERO_KG)
				.add(totalWaste.orElse(AmountWithUnit.ZERO_KG))
				.add(totalQC.orElse(AmountWithUnit.ZERO_KG))
				.subtract(totalProductIn.orElse(AmountWithUnit.ZERO_KG))
				.subtract(totalIngredients.orElse(AmountWithUnit.ZERO_KG));
	}

	private Optional<AmountWithUnit> getTotalWeight(List<ItemAmount> itemAmounts) {
		if(itemAmounts == null)
			return Optional.empty();
		return itemAmounts.stream().map(i -> i.getWeight()[0]).reduce(AmountWithUnit::add);
//		return new AmountWithUnit[] {
//				totalWeight.convert(MeasureUnit.KG),
//				totalWeight.convert(MeasureUnit.LBS)};
	}


}
