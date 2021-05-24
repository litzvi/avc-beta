/**
 * 
 */
package com.avc.mis.beta.dto.exportdoc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class InventoryExportDoc {
	
	private ExportInfo exportInfo;

	private List<ContainerPoItemRow> loadedTotals;
	
	public List<AmountWithUnit> getNetTotal() {
		if(getLoadedTotals() != null) {
			Optional<AmountWithUnit> optionalWeight = loadedTotals.stream()
					.map(i -> i.getTotal())
					.filter(j -> MeasureUnit.WEIGHT_UNITS.contains(j.getMeasureUnit()))
					.reduce(AmountWithUnit::add);
			
			if(optionalWeight.isPresent()) {
				return AmountWithUnit.weightDisplay(optionalWeight.get(), Arrays.asList(MeasureUnit.LBS, MeasureUnit.KG));			
			}
		}
		return null;
	}
}
