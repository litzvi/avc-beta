/**
 * 
 */
package com.avc.mis.beta.dto.query;

import com.avc.mis.beta.dto.item.BillOfMaterialsDTO;
import com.avc.mis.beta.dto.item.BomLineDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class BomProductWithMaterialLine {

	private BillOfMaterialsDTO billOfMaterials;
	private BomLineDTO bomLine;
	
	public BomProductWithMaterialLine(Integer bomId, Integer productId, String productValue, AmountWithUnit defaultBatch, 
			Integer lineId, Integer lineOrdinal, 
			Integer materialId, String meterialValue, AmountWithUnit defaultAmount) {
		super();
		this.billOfMaterials = new BillOfMaterialsDTO(bomId, productId, productValue, defaultBatch);
		this.bomLine = new BomLineDTO(lineId, lineOrdinal, materialId, meterialValue, defaultAmount);
	}
}
