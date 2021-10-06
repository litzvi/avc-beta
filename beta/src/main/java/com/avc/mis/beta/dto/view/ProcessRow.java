/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
	
	@Getter(value = AccessLevel.NONE)
	private final static MeasureUnit RATION_CALCULATION_MEASURE_UNIT = MeasureUnit.LBS;

	@Getter(value = AccessLevel.NONE)
	private List<MeasureUnit> displayMeasureUnits = Arrays.asList(MeasureUnit.LBS);


	private String processName;
	private String productionLine;
//	private PoCodeBasic poCode;//should be removed
	private int[] poCodeIds;
	private String[] poCodes;
	private String[] suppliers;
//	private String supplierName;//should be removed
	private LocalDateTime recordedTime;
	private LocalTime startTime;
	private LocalTime endTime;
	private Duration downtime;
	private Integer numOfWorkers;
	private ProcessStatus status;
	private String[] approvals;
	private String remarks;
	
	private List<ProductionProcessWithItemAmount> usedItems;
	@JsonIgnore private Optional<AmountWithUnit> usedAmounts;
	
	private List<ProductionProcessWithItemAmount> producedItems;
	@JsonIgnore private Optional<AmountWithUnit> producedAmounts;
		
	private List<ProductionProcessWithItemAmount> itemCounts;
	@JsonIgnore private Optional<AmountWithUnit> countAmounts;
	
	public ProcessRow(@NonNull Integer id, String processName, String productionLine, 
//			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, String display,
			String poCodeIds, String poCodes, String suppliers,
			LocalDateTime recordedTime, 
			LocalTime startTime, LocalTime endTime, Duration downtime, Integer numOfWorkers, 
			ProcessStatus status, String approvals, String remarks) {
		super(id);
		this.processName = processName;
		this.productionLine = productionLine;
//		if(poCodeId != null)
//			this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, display);
		if(poCodeIds != null)
			this.poCodeIds = Stream.of(poCodeIds.split(",")).mapToInt(j -> Integer.valueOf(j)).toArray();
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).toArray(String[]::new);
		if(suppliers != null)
			this.suppliers = Stream.of(suppliers.split(",")).toArray(String[]::new);
//		this.supplierName = supplierName;
		this.recordedTime = recordedTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.downtime = downtime;
		this.numOfWorkers = numOfWorkers;
		this.status = status;
		if(approvals == null || approvals.startsWith(":")) {
			this.approvals = null;
		}
		else {
			this.approvals = Stream.of(approvals.split(",")).toArray(String[]::new);
		}
		this.remarks = remarks;
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
			List<AmountWithUnit> diff =  AmountWithUnit.weightDisplay(processGain, displayMeasureUnits);
			diff.add(0, getProcessPercentageGain());
			return diff;
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
	}
	
	public AmountWithUnit getProcessPercentageGain() {
		try {
			BigDecimal percentageGain = AmountWithUnit.percentageLoss(producedAmounts.get(), usedAmounts.get());
			return new AmountWithUnit(percentageGain, MeasureUnit.PERCENT);
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
	}	
		
	//perhaps change to getCountDifference
	public List<AmountWithUnit> getUsedCountDifference() {
		
		try {
			AmountWithUnit countDifference = countAmounts.get().subtract(usedAmounts.get());			
			List<AmountWithUnit> diff = AmountWithUnit.weightDisplay(countDifference, displayMeasureUnits);
			diff.add(0, getUsedCountPercentageDifference());
			return diff;
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
		
	}
	
	public AmountWithUnit getUsedCountPercentageDifference() {
		try {
			BigDecimal percentageLoss = AmountWithUnit.percentageLoss(countAmounts.get(), usedAmounts.get());
			return new AmountWithUnit(percentageLoss, MeasureUnit.PERCENT);
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
	}
	
	public List<AmountWithUnit> getProducedCountDifference() {
		
		try {
			AmountWithUnit countDifference = countAmounts.get().subtract(producedAmounts.get());
			List<AmountWithUnit> diff =  AmountWithUnit.weightDisplay(countDifference, displayMeasureUnits);
			diff.add(0, getProducedCountPercentageDifference());
			return diff;
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
		
	}
	
	public AmountWithUnit getProducedCountPercentageDifference() {
		try {
			BigDecimal percentageDifference = AmountWithUnit.percentageLoss(countAmounts.get(), producedAmounts.get());
			return new AmountWithUnit(percentageDifference, MeasureUnit.PERCENT);
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
	}
	
	public Ratio getUniformTotals() {
		Ratio ratio = new Ratio();
		if(getUsedAmounts() != null && getUsedAmounts().isPresent())
			ratio.setUsed(getUsedAmounts().get().convert(RATION_CALCULATION_MEASURE_UNIT).getAmount());
		if(getProducedAmounts() != null && getProducedAmounts().isPresent())
			ratio.setProduced(getProducedAmounts().get().convert(ProcessRow.RATION_CALCULATION_MEASURE_UNIT).getAmount());
		if(getCountAmounts() != null && getCountAmounts().isPresent())
			ratio.setCount(getCountAmounts().get().convert(ProcessRow.RATION_CALCULATION_MEASURE_UNIT).getAmount());
		if(ratio.getProduced() != null && ratio.getUsed() != null)
			ratio.setLoss(ratio.getProduced().subtract(ratio.getUsed()));
		else if(ratio.getProduced() != null && ratio.getCount() != null)
			ratio.setLoss(ratio.getProduced().subtract(ratio.getCount()));
		return ratio;
	}
	
	@Data
	private class Ratio {
		BigDecimal used;
		BigDecimal produced;
		BigDecimal count;
		BigDecimal loss;
	}
	
}
