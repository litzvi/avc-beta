/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class UsedItemTableDTO {
	
	private BasicValueEntity<Item> item;
	private PoCodeDTO itemPo;
	private OffsetDateTime itemProcessDate;
	
	private List<BasicUsedStorageDTO> amounts;
	
	private MeasureUnit measureUnit;
	private BigDecimal containerWeight;	
	
	private Warehouse warehouseLocation;
	
}
