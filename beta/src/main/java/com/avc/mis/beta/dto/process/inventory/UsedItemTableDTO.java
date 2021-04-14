/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
public class UsedItemTableDTO {
	
	private BasicValueEntity<Item> item;
	private PoCodeBasic itemPo;
	private OffsetDateTime itemProcessDate;
	
	private List<BasicUsedStorageDTO> amounts;
	
	private MeasureUnit measureUnit;
//	private BigDecimal accessWeight;	
	
	private Warehouse warehouseLocation;
	
	@EqualsAndHashCode.Exclude
	private String[] itemPoCodes;
	@EqualsAndHashCode.Exclude
	private String[] itemSuppliers;
	
}
