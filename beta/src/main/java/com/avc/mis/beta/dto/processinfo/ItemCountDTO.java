/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.query.ItemCountWithAmount;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ItemCount;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemCountDTO extends SubjectDataDTO {

	private ItemDTO item;
	private MeasureUnit measureUnit;
	private BigDecimal containerWeight;	
	
	private List<CountAmountDTO> amounts;

	public ItemCountDTO(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ItemCategory itemCategory,
			MeasureUnit measureUnit, BigDecimal containerWeight) {
		super(id, version, ordinal);
		this.item = new ItemDTO(itemId, itemValue, null, null, itemCategory);
		this.measureUnit = measureUnit;
		this.containerWeight = containerWeight;
	}

	/**
	 * @param itemCount
	 */
	public ItemCountDTO(ItemCount itemCount) {
		super(itemCount.getId(), itemCount.getVersion(), itemCount.getOrdinal());
		this.item = new ItemDTO(itemCount.getItem());
		this.measureUnit = itemCount.getMeasureUnit();
		this.containerWeight = itemCount.getContainerWeight();
		setAmounts(Arrays.stream(itemCount.getAmounts())
				.map(i->{return new CountAmountDTO(i);})
				.collect(Collectors.toList()));
	}
	
	public AmountWithUnit[] getTotalAmount() {
		if(this.amounts == null) {
			return null;
		}
		BigDecimal total = this.amounts.stream()
				.map(i -> i.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		if(this.containerWeight != null) {
			total = total.subtract(this.containerWeight.multiply(new BigDecimal(this.amounts.size()), MathContext.DECIMAL64));
		}
		//need to reduce accessWeight when changed to Item count
		AmountWithUnit totalAmount = new AmountWithUnit(total, this.measureUnit);
		return new AmountWithUnit[] {totalAmount.setScale(MeasureUnit.SCALE),
				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
	}
	
	
	public static List<ItemCountDTO> getItemCounts(List<ItemCountWithAmount> amounts) {
		Map<Integer, List<ItemCountWithAmount>> map = amounts.stream()
				.collect(Collectors.groupingBy(ItemCountWithAmount::getId, Collectors.toList()));
		List<ItemCountDTO> itemCounts = new ArrayList<>();
		for(List<ItemCountWithAmount> list: map.values()) {
			ItemCountDTO itemCount = list.get(0).getItemCount();
			itemCount.setAmounts(list.stream().map(i -> i.getAmount())
					.sorted(Ordinal.ordinalComparator())
					.collect(Collectors.toList()));
			itemCounts.add(itemCount);
		}
		itemCounts.sort(Ordinal.ordinalComparator());
		return itemCounts;
	}
	
	
}
