/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessRow extends ValueDTO {

	private PoCodeBasic poCode;
	private String supplierName;
	private OffsetDateTime recordedTime;
	private Duration duration;
	 
	private List<ProductionProcessWithItemAmount> usedItems;
	@JsonIgnore private Optional<AmountWithUnit> usedAmounts;
	
	private List<ProductionProcessWithItemAmount> producedItems;
	@JsonIgnore private Optional<AmountWithUnit> producedAmounts;
		
	private List<ProductionProcessWithItemAmount> itemCounts;
	@JsonIgnore private Optional<AmountWithUnit> countAmounts;
	
	public ProcessRow(@NonNull Integer id, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			OffsetDateTime recordedTime, Duration duration) {
		super(id);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.supplierName = supplierName;
		this.recordedTime = recordedTime;
		this.duration = duration;
		
	}
	
	public void setUsedItems(List<ProductionProcessWithItemAmount> usedItems) {
		this.usedItems = usedItems;
		if(usedItems != null) {
			this.usedAmounts = getUsedItems().stream()
					.map(i -> i.getAmountWithUnit()[0])
					.reduce(AmountWithUnit::add);
		}
	}
	
	public void setProducedItems(List<ProductionProcessWithItemAmount> producedItems) {
		this.producedItems = producedItems;
		if(producedItems != null) {
			this.producedAmounts = getProducedItems().stream()
					.map(i -> i.getAmountWithUnit()[0])
					.reduce(AmountWithUnit::add);
		}
	}

	public void setItemCounts(List<ProductionProcessWithItemAmount> itemCounts) {
		this.itemCounts = itemCounts;
		if(itemCounts != null) {
			this.countAmounts = getItemCounts().stream()
					.map(i -> i.getAmountWithUnit()[0])
					.reduce(AmountWithUnit::add);
		}
	}
	
	public AmountWithUnit[] getProcessGain() {
		
		try {
			AmountWithUnit processGain = producedAmounts.get().substract(usedAmounts.get());
			return new AmountWithUnit[] {
					processGain.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE),                        
					processGain.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE)
			};
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
		

	}
	
		
	//perhaps change to getCountDifference
	public AmountWithUnit[] getUsedCountDifference() {
		
		try {
			AmountWithUnit countDifference = countAmounts.get().substract(usedAmounts.get());
			return new AmountWithUnit[] {
					countDifference.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE),                        
					countDifference.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE)
			};
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
		
	}
	
	public AmountWithUnit[] getProducedCountDifference() {
		
		try {
			AmountWithUnit countDifference = countAmounts.get().substract(producedAmounts.get());
			return new AmountWithUnit[] {
					countDifference.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE),                        
					countDifference.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE)
			};
		} catch (NullPointerException | NoSuchElementException e) {
			return null;
		}
		
	}
	
	
}
