/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.LoadingReportLine;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.values.Item;

import lombok.Value;

/**
 * Used for fetching loading information for final report
 * 
 * @author zvi
 *
 */
@Value
public class ItemAmountWithLoadingReportLine {
	
	LoadingReportLine loadingReportLine;
	ItemAmount itemAmount;
	
	public ItemAmountWithLoadingReportLine(
			Integer processId, Integer shipmentId, String shipmentCode, String portOfDischargeCode, String portOfDischargeValue, 
			ContainerDetails containerDetails, LocalDateTime loadingDate, ProcessStatus status, String approvals,
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz, 
			BigDecimal amount, BigDecimal weightCoefficient
			) {
		this.loadingReportLine = new LoadingReportLine(processId, shipmentId, shipmentCode, portOfDischargeCode, portOfDischargeValue, containerDetails, loadingDate, status, approvals);
		this.itemAmount = new ItemAmount(itemId, itemValue, defaultMeasureUnit, itemGroup, productionUse, unitAmount, unitMeasureUnit, clazz, 
				amount, weightCoefficient);
	}

}
