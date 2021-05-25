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
public class SecurityExportDoc {
	
	private ExportInfo exportInfo;

	private List<ContainerPoItemStorageRow> loadedStorages;

	
	public List<AmountWithUnit> getGrossTotal() {
		if(getLoadedStorages() != null) {
			Optional<AmountWithUnit> optionalWeight = loadedStorages.stream()
					.map(i -> i.getTotalWeight())
					.filter(j -> j != null && MeasureUnit.WEIGHT_UNITS.contains(j.getMeasureUnit()))
					.reduce(AmountWithUnit::add);
			
			if(optionalWeight.isPresent()) {
				return AmountWithUnit.weightDisplay(optionalWeight.get(), Arrays.asList(MeasureUnit.LBS, MeasureUnit.KG));			
			}
		}
		return null;
	}
}
