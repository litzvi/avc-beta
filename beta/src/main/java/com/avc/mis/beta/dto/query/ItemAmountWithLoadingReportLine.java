/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.LoadingReportLine;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ItemAmountWithLoadingReportLine implements CollectionItemWithGroup<ItemAmount, LoadingReportLine> {
	
	LoadingReportLine loadingReportLine;
	ItemAmount itemAmount;
	
	public ItemAmountWithLoadingReportLine(
			Integer id, String portOfDischargeCode, String portOfDischargeValue, 
			ContainerDetails containerDetails, OffsetDateTime loadingDate, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz, 
			BigDecimal amount) {
		this.loadingReportLine = new LoadingReportLine(id, portOfDischargeCode, portOfDischargeValue, containerDetails, loadingDate);
		this.itemAmount = new ItemAmount(itemId, itemValue, defaultMeasureUnit, itemGroup, productionUse, unitAmount, unitMeasureUnit, clazz, amount);
	}

	@Override
	public ItemAmount getItem() {
		return this.itemAmount;
	}

	@Override
	public LoadingReportLine getGroup() {
		return this.loadingReportLine;
	}
	
}
