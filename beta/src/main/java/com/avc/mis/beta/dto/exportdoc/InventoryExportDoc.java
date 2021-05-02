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
public class InventoryExportDoc {
	
	private ExportInfo exportInfo;

	private List<AmountWithUnit> netTotal;
	private List<ContainerPoItemRow> loadedTotals;
		
	public void setLoadedTotals(List<ContainerPoItemRow> loadedTotals) {
		this.loadedTotals = loadedTotals;
		AmountWithUnit totalLbs = loadedTotals.stream()
				.map(i -> i.getTotal())
				.reduce(AmountWithUnit.ZERO_LBS, AmountWithUnit::add);
		this.netTotal = AmountWithUnit.weightDisplay(totalLbs, Arrays.asList(MeasureUnit.LBS, MeasureUnit.KG));
		
	}
}
