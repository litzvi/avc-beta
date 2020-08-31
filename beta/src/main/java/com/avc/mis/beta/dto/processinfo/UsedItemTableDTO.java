/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.List;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
public class UsedItemTableDTO {
	
	private BasicValueEntity<Item> item;
	private PoCodeDTO itemPo;
	
	private List<BasicUsedStorageDTO> amounts;
	
	private MeasureUnit measureUnit;
	private BigDecimal containerWeight;	
	
	private Warehouse warehouseLocation;
	
}
