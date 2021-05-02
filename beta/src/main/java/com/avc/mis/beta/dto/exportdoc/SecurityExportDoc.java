/**
 * 
 */
package com.avc.mis.beta.dto.exportdoc;

import java.util.Arrays;
import java.util.List;

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

	private List<AmountWithUnit> grossTotal;
	private List<ContainerPoItemStorageRow> loadedStorages;

	public void setLoadedStorages(List<ContainerPoItemStorageRow> loadedStorages) {
		this.loadedStorages = loadedStorages;
		AmountWithUnit totalLbs = loadedStorages.stream()
				.map(i -> i.getTotalWeight())
				.reduce(AmountWithUnit.ZERO_LBS, AmountWithUnit::add);
		this.grossTotal = AmountWithUnit.weightDisplay(totalLbs, Arrays.asList(MeasureUnit.LBS, MeasureUnit.KG));
	}
	
	
}
