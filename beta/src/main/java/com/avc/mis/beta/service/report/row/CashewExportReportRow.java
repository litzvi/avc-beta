/**
 * 
 */
package com.avc.mis.beta.service.report.row;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SaltLevel;
import com.avc.mis.beta.entities.enums.ShippingContainerType;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class CashewExportReportRow extends CashewBaggedInventoryRow {


//	@JsonIgnore Integer usedProcess;
	String poCode;
	LocalDateTime processDate;
	
	ShipmentCodeBasic shipmentCode;
	LocalDate eta;
	String containerNumber;
	String sealNumber;
	String containerSize;
	
	String remarks;

	public CashewExportReportRow(Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, ItemGroup itemGroup,
			ProductionUse productionUse, AmountWithUnit unit, Class<? extends Item> clazz, 
			String brand, String code, boolean whole, boolean roast, boolean toffee,
			Integer gradeId,  String gradeValue,
			SaltLevel saltLevel, int numBags, 
			BigDecimal amount, MeasureUnit measureUnit, BigDecimal boxQuantity, //BigDecimal weightCoefficient, 
//			Integer usedProcess, 
			String poCode, LocalDateTime processDate, 
			Integer shipmentCodeId, String shipmentCodeCode, String portOfDischargeCode, String portOfDischargeValue, 
			LocalDate eta, String containerNumber, String sealNumber, ShippingContainerType containerType, String remarks) {
		super(itemId, itemValue, defaultMeasureUnit, itemGroup, productionUse, unit, clazz,
				brand, code, whole, roast, toffee, gradeId, gradeValue, saltLevel, numBags, amount, measureUnit, boxQuantity);
//		this.usedProcess = usedProcess;
		this.poCode = poCode;
		this.processDate = processDate;
		this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, shipmentCodeCode, portOfDischargeCode, portOfDischargeValue);
		this.eta = eta;
		this.containerNumber = containerNumber;
		this.sealNumber = sealNumber;
		if(containerType != null)
			this.containerSize = containerType.toString();
		else
			this.containerSize = null;
		this.remarks = remarks;
	}
	
}
