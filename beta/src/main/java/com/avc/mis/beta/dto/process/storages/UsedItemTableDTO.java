/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.time.LocalDateTime;
import java.util.List;

import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.CashewGrade;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * List of used items in table format.
 * Used when using storages inserted as a storage table, therefore belong to the same process item.
 * 
 * @author zvi
 *
 */
@Data
public class UsedItemTableDTO {
	
	private BasicValueEntity<Item> item;
	private PoCodeBasic itemPo;
	private LocalDateTime itemProcessDate;
	
	private List<BasicUsedStorageDTO> amounts;
	
	private MeasureUnit measureUnit;
//	private BigDecimal accessWeight;	
	
	private BasicValueEntity<Warehouse> warehouseLocation;
	
	@EqualsAndHashCode.Exclude
	private String[] itemPoCodes;
	@EqualsAndHashCode.Exclude
	private String[] itemSuppliers;
	@EqualsAndHashCode.Exclude
	private BasicValueEntity<CashewGrade> cashewGrade;

}
