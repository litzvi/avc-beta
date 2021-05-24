/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.values.ItemWithUse;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.collection.ItemCount;
import com.avc.mis.beta.utilities.ListGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO Class representing a count/weight check for inventory check,
 * but dosen't actually influence inventory balance.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemCountDTO extends SubjectDataDTO implements ListGroup<CountAmountDTO> {

	private ItemWithUse item;
	private MeasureUnit measureUnit;
	private BigDecimal containerWeight;
	private BigDecimal accessWeight;
	
	private List<CountAmountDTO> amounts;

	public ItemCountDTO(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> clazz,
			MeasureUnit measureUnit, BigDecimal containerWeight, BigDecimal accessWeight) {
		super(id, version, ordinal);
		this.item = new ItemWithUse(itemId, itemValue, productionUse, clazz);
		this.measureUnit = measureUnit;
		this.containerWeight = containerWeight;
		this.accessWeight = accessWeight;
	}

	/**
	 * @param itemCount
	 */
	public ItemCountDTO(ItemCount itemCount) {
		super(itemCount.getId(), itemCount.getVersion(), itemCount.getOrdinal());
		this.item = new ItemWithUse(itemCount.getItem());
		this.measureUnit = itemCount.getMeasureUnit();
		this.containerWeight = itemCount.getContainerWeight();
		this.accessWeight = itemCount.getAccessWeight();
		setAmounts(Arrays.stream(itemCount.getAmounts())
				.map(i->{return new CountAmountDTO(i);})
				.collect(Collectors.toList()));
	}
	
	public List<AmountWithUnit> getTotalAmount() {
		if(this.amounts == null) {
			return null;
		}
		BigDecimal total = this.amounts.stream()
				.map(i -> i.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		if(this.containerWeight != null) {
			total = total.subtract(this.containerWeight.multiply(new BigDecimal(this.amounts.size()), MathContext.DECIMAL64));
		}
		if(this.accessWeight != null) {
			total = total.subtract(this.accessWeight);
		}
		AmountWithUnit totalAmount = new AmountWithUnit(total, this.measureUnit);
		return AmountWithUnit.weightDisplay(totalAmount, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
//		return new AmountWithUnit[] {totalAmount.setScale(MeasureUnit.SCALE),
//				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
	}
	
	
//	public static List<ItemCountDTO> getItemCounts(List<ItemCountWithAmount> amounts) {
//		Map<Integer, List<ItemCountWithAmount>> map = amounts.stream()
//				.collect(Collectors.groupingBy(ItemCountWithAmount::getId, LinkedHashMap::new, Collectors.toList()));
//		List<ItemCountDTO> itemCounts = new ArrayList<>();
//		for(List<ItemCountWithAmount> list: map.values()) {
//			ItemCountDTO itemCount = list.get(0).getItemCount();
//			itemCount.setAmounts(list.stream().map(i -> i.getAmount())
////					.sorted(Ordinal.ordinalComparator())
//					.collect(Collectors.toList()));
//			itemCounts.add(itemCount);
//		}
////		itemCounts.sort(Ordinal.ordinalComparator());
//		return itemCounts;
//	}

	@JsonIgnore
	@Override
	public void setList(List<CountAmountDTO> list) {
		setAmounts(list);
	}
	
	
}
