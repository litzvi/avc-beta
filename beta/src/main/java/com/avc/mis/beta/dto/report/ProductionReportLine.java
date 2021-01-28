/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

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
public class ProductionReportLine extends ProductReportLine {
	
//	public ProductionReportLine(@NonNull Integer id) {
//		super(id);
//	}
	
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
		this.totalProductIn = empty ? null : ItemAmount.getTotalWeight(productIn);
	}

	public void setIngredients(List<ItemAmount> ingredients) {
		boolean empty = ingredients == null || ingredients.isEmpty();
		this.ingredients = empty ? null : ingredients;
		this.totalIngredients = empty ? null : ItemAmount.getTotalWeight(ingredients);
	}

	public void setProductOut(List<ItemAmount> productOut) {
		boolean empty = productOut == null || productOut.isEmpty();
		this.productOut = empty ? null : productOut;
		this.totalProductOut = empty ? null : ItemAmount.getTotalWeight(productOut);
	}

	public void setWaste(List<ItemAmount> waste) {
		boolean empty = waste == null || waste.isEmpty();
		this.waste = empty ? null : waste;
		this.totalWaste = empty ? null : ItemAmount.getTotalWeight(waste);
	}

	public void setQc(List<ItemAmount> qc) {
		boolean empty = qc == null || qc.isEmpty();
		this.qc = empty ? null : qc;
		this.totalQC = empty ? null : ItemAmount.getTotalWeight(qc);
	}
	
	private AmountWithUnit getTotalOut() {
		return AmountWithUnit.addNullable(AmountWithUnit.addNullable(totalProductOut, totalWaste), totalQC);
	}
	
	private AmountWithUnit getTotalIn() {
		return AmountWithUnit.addNullable(totalProductIn, totalIngredients);
	}

	public AmountWithUnit getDifference() {		
		return AmountWithUnit.subtractNullable(getTotalOut(), getTotalIn());
	}
	
	public BigDecimal getRatioLoss() {
		return AmountWithUnit.divide(getTotalOut(), getTotalIn()).setScale(MeasureUnit.DIVISION_SCALE, RoundingMode.HALF_DOWN);
	}
	
	public BigDecimal getProductRatioLoss() {
		return AmountWithUnit.divide(totalProductOut, totalProductIn).setScale(MeasureUnit.DIVISION_SCALE, RoundingMode.HALF_DOWN);
	}


}
