/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.LoadingReportLine;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.Data;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
//@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ItemAmountWithLoadingReportLine implements CollectionItemWithGroup<ItemAmount, LoadingReportLine> {
	
	LoadingReportLine loadingReportLine;
	ItemAmount itemAmount;
	
	public ItemAmountWithLoadingReportLine(
			Integer processId, Integer shipmentId, String shipmentCode, String portOfDischargeCode, String portOfDischargeValue, 
			ContainerDetails containerDetails, OffsetDateTime loadingDate, ProcessStatus status, String approvals,
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz, 
			BigDecimal amount) {
		this.loadingReportLine = new LoadingReportLine(processId, shipmentId, shipmentCode, portOfDischargeCode, portOfDischargeValue, containerDetails, loadingDate, status, approvals);
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
