/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.group.SampleItemDTO;
import com.avc.mis.beta.dto.query.SampleItemWithWeight;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.SampleReceipt;
import com.avc.mis.beta.entities.process.group.SampleItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Deprecated
public class SampleReceiptDTO extends PoProcessDTO {

	//not set because we can have doubles, order should be unimportant for testing - so bag needed
	private List<SampleItemDTO> sampleItems; 	
	
	public void setSampleItems(List<SampleItemDTO> sampleItems) {
		this.sampleItems = sampleItems;
	}
	
	public void setSampleItemsWithWeight(List<SampleItemWithWeight> sampleItems) {
		Map<Integer, List<SampleItemWithWeight>> map = sampleItems.stream()
				.collect(Collectors.groupingBy(SampleItemWithWeight::getId, LinkedHashMap::new, Collectors.toList()));
			this.sampleItems = new ArrayList<SampleItemDTO>();
			for(List<SampleItemWithWeight> list: map.values()) {
				SampleItemDTO sampleItem = list.get(0).getSampleItem();
				sampleItem.setItemWeights(list.stream()
						.map(i -> i.getItemWeight())
						.sorted(Ordinal.ordinalComparator())
						.collect(Collectors.toList()));
				this.sampleItems.add(sampleItem);
			}
//			this.sampleItems.sort(Ordinal.ordinalComparator());
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return SampleReceipt.class;
	}
	
	@Override
	public SampleReceipt fillEntity(Object entity) {
		SampleReceipt sample;
		if(entity instanceof SampleReceipt) {
			sample = (SampleReceipt) entity;
		}
		else {
			throw new IllegalStateException("Param has to be SampleReceipt class");
		}
		super.fillEntity(sample);

		if(getSampleItems() != null) {
			Ordinal.setOrdinals(getSampleItems());
			sample.setSampleItems(getSampleItems().stream().map(i -> i.fillEntity(new SampleItem())).collect(Collectors.toSet()));
		}
		
		return sample;
	}

}
