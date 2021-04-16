/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ProcessRow extends BasicDTO {

//	private PoCodeBasic poCode;//should be removed
	private int[] poCodeIds;
	private String[] poCodes;
	private String[] suppliers;
//	private String supplierName;//should be removed
	private OffsetDateTime recordedTime;
	private Duration duration;
	private ProcessStatus status;
	private String[] approvals;
	 
	private List<ProductionProcessWithItemAmount> usedItems;
	private Optional<AmountWithUnit> usedAmounts;
	
	private List<ProductionProcessWithItemAmount> producedItems;
	private Optional<AmountWithUnit> producedAmounts;
		
	private List<ProductionProcessWithItemAmount> itemCounts;
	private Optional<AmountWithUnit> countAmounts;
	
	public ProcessRow(@NonNull Integer id, 
//			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, String display,
			String poCodeIds, String poCodes, String suppliers,
			OffsetDateTime recordedTime, Duration duration, ProcessStatus status, String approvals) {
		super(id);
//		if(poCodeId != null)
//			this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, display);
		if(poCodeIds != null)
			this.poCodeIds = Stream.of(poCodeIds.split(",")).filter(i -> i != null).mapToInt(j -> Integer.valueOf(j)).distinct().toArray();
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).distinct().toArray(String[]::new);
		if(suppliers != null)
			this.suppliers = Stream.of(suppliers.split(",")).distinct().toArray(String[]::new);
//		this.supplierName = supplierName;
		this.recordedTime = recordedTime;
		this.duration = duration;
		this.status = status;
		if(approvals == null || approvals.startsWith(":")) {
			this.approvals = null;
		}
		else {
			this.approvals = Stream.of(approvals.split(",")).distinct().toArray(String[]::new);
		}
	}
	
	public void setUsedItems(List<ProductionProcessWithItemAmount> usedItems) {
		this.usedItems = usedItems;
		if(usedItems != null) {
			this.usedAmounts = ProductionProcessWithItemAmount.getWeightSum(usedItems);
//			this.usedAmounts = getUsedItems().stream()
//					.map(i -> i.getWeight()[0])
//					.reduce(AmountWithUnit::add);
		}
	}
	
	public void setProducedItems(List<ProductionProcessWithItemAmount> producedItems) {
		this.producedItems = producedItems;
		if(producedItems != null) {
			this.producedAmounts = ProductionProcessWithItemAmount.getWeightSum(producedItems);
//			this.producedAmounts = getProducedItems().stream()
//					.map(i -> i.getWeight()[0])
//					.reduce(AmountWithUnit::add);
		}
	}

	public void setItemCounts(List<ProductionProcessWithItemAmount> itemCounts) {
		this.itemCounts = itemCounts;
		if(itemCounts != null) {
			this.countAmounts = ProductionProcessWithItemAmount.getWeightSum(itemCounts);
//			this.countAmounts = getItemCounts().stream()
//					.map(i -> i.getWeight()[0])
//					.reduce(AmountWithUnit::add);
		}
	}
	
	public List<AmountWithUnit> getProcessGain() {
		
		try {
			AmountWithUnit processGain = producedAmounts.get().subtract(usedAmounts.get());
			List<AmountWithUnit> diff =  AmountWithUnit.weightDisplay(processGain, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
			diff.add(0, new AmountWithUnit(getProcessPercentageGain(), MeasureUnit.PERCENT));
			return diff;
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
		

	}
	
	public BigDecimal getProcessPercentageGain() {
		try {
			return AmountWithUnit.percentageLoss(producedAmounts.get(), usedAmounts.get());
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
	}	
		
	//perhaps change to getCountDifference
	public List<AmountWithUnit> getUsedCountDifference() {
		
		try {
			AmountWithUnit countDifference = countAmounts.get().subtract(usedAmounts.get());			
			List<AmountWithUnit> diff = AmountWithUnit.weightDisplay(countDifference, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
			diff.add(0, new AmountWithUnit(getUsedCountPercentageDifference(), MeasureUnit.PERCENT));
			return diff;
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
		
	}
	
	public BigDecimal getUsedCountPercentageDifference() {
		try {
			return AmountWithUnit.percentageLoss(countAmounts.get(), usedAmounts.get());
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
	}
	
	public List<AmountWithUnit> getProducedCountDifference() {
		
		try {
			AmountWithUnit countDifference = countAmounts.get().subtract(producedAmounts.get());
			List<AmountWithUnit> diff =  AmountWithUnit.weightDisplay(countDifference, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
			diff.add(0, new AmountWithUnit(getProducedCountPercentageDifference(), MeasureUnit.PERCENT));
			return diff;
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
		
	}
	
	public BigDecimal getProducedCountPercentageDifference() {
		try {
			return AmountWithUnit.percentageLoss(countAmounts.get(), producedAmounts.get());
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
	}
	
	
}
