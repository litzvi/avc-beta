/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.process.StorageDTO;
import com.avc.mis.beta.dto.values.ValueObject;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessItemWithStorage extends ProcessDTO {

	private ValueObject item;
	private PoCodeDTO itemPo;
	
	private Integer storageId;
	private Integer storageVersion;
	private BigDecimal unitAmount;
	private MeasureUnit measureUnit;
	private BigDecimal numberUnits;	
	private ValueObject warehouseLocation;
	private String description;
	private String remarks;
	
	
	public ProcessItemWithStorage(Integer id, Integer version, Integer itemId, String itemValue, 
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, 
			Integer storageId, Integer storageVersion,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue,
			String description, String remarks) {
		super(id, version);
		this.item = new ValueObject(itemId, itemValue);
		if(poCodeId != null)
			this.itemPo = new PoCodeDTO(poCodeId, contractTypeCode, supplierName);
//		if(itemPo != null)
//			this.itemPo = new PoCodeDTO(poCode);
//		else
//			this.itemPo = null;
		
		this.storageId = storageId;
		this.storageVersion = storageVersion;
		this.unitAmount = unitAmount.setScale(3);
		this.measureUnit = measureUnit;
		this.numberUnits = numberUnits.setScale(3);
		if(warehouseLocationId != null)
			this.warehouseLocation = new ValueObject(warehouseLocationId,  warehouseLocationValue);
		this.description = description;
		this.remarks = remarks;
		
	}
	
	public StorageDTO getStorage() {
		return new StorageDTO(storageId, storageVersion, 
				unitAmount, measureUnit, numberUnits, warehouseLocation, description);
	}

}
