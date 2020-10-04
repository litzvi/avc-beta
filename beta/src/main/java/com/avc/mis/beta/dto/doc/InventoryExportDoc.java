/**
 * 
 */
package com.avc.mis.beta.dto.doc;

import java.util.List;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@ToString(callSuper = true)
public class InventoryExportDoc{
	
	private ExportInfo exportInfo;

	private AmountWithUnit[] netTotal;
	private List<ContainerPoItemRow> loadedTotals;
		
	public void setLoadedTotals(List<ContainerPoItemRow> loadedTotals) {
		this.loadedTotals = loadedTotals;
		AmountWithUnit totalLbs = loadedTotals.stream()
				.map(i -> i.getTotalRow()[0])
				.reduce(AmountWithUnit.ZERO_LBS, AmountWithUnit::add);
		this.netTotal = new AmountWithUnit[] {
				totalLbs.setScale(MeasureUnit.SCALE),
				totalLbs.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE)
		};
		
	}


}
