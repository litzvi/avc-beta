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
public class SecurityExportDoc {
	
	private ExportInfo exportInfo;

	private AmountWithUnit[] grossTotal;
	private List<ContainerPoItemStorageRow> loadedStorages;

	public void setLoadedStorages(List<ContainerPoItemStorageRow> loadedStorages) {
		this.loadedStorages = loadedStorages;
		AmountWithUnit totalLbs = loadedStorages.stream()
				.map(i -> i.getUnitAmount().multiply(i.getNumberUnits()))
				.reduce(AmountWithUnit.ZERO_LBS, AmountWithUnit::add);
		this.grossTotal = new AmountWithUnit[] {
				totalLbs.setScale(MeasureUnit.SCALE),
				totalLbs.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE)
		};
	}
	
	
}
