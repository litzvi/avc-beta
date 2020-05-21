/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.ProcessItem;
import com.avc.mis.beta.entities.process.ReceiptItem;
import com.avc.mis.beta.entities.values.Item;
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
public class ProcessItemDTO extends ProcessDTO {

	private Item item;
	private PoCodeBasic itemPo;
	
//	BigDecimal unitAmount;
//	MeasureUnit measureUnit;
//	BigDecimal numberUnits;	
//	Warehouse storageLocation;
	private String description;
	private String remarks;
	
	private Set<StorageDTO> storageForms; //can use a SortedSet like ContactDetails to maintain order
	
	public ProcessItemDTO(Integer id, Integer version, Item item, PoCode itemPo,
			/*BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, Warehouse storageLocation, */
			String description, String remarks) {
		super(id, version);
		this.item = item;
		if(itemPo != null)
			this.itemPo = new PoCodeBasic(itemPo);
		else
			this.itemPo = null;
//		this.unitAmount = unitAmount.setScale(3);
//		this.measureUnit = measureUnit;
//		this.numberUnits = numberUnits.setScale(3);
//		this.storageLocation = storageLocation;
		this.description = description;
		this.remarks = remarks;
		
//		this.unitAmount.setScale(3);//for testing with assertEquals
//		this.numberUnits.setScale(3);//for testing with assertEquals
		
	}
	
	
	/**
	 * @param processItem
	 */
	public ProcessItemDTO(ProcessItem processItem) {
		super(processItem.getId(), processItem.getVersion());
		this.item = processItem.getItem();
		if(processItem.getItemPo() != null)
			this.itemPo = new PoCodeBasic(processItem.getItemPo());
		else
			this.itemPo = null;
//		this.measureUnit = processItem.getMeasureUnit();
//		this.unitAmount = processItem.getUnitAmount().setScale(3);
//		this.numberUnits = processItem.getNumberUnits().setScale(3);
//		this.storageLocation = processItem.getStorageLocation();
		this.description = processItem.getDescription();
		this.remarks = processItem.getRemarks();
		
		this.storageForms = Arrays.stream(processItem.getStorageForms())
				.map(i->{return new StorageDTO(i);}).collect(Collectors.toSet());

		
//		this.unitAmount.setScale(3);//for testing with assertEquals
//		this.numberUnits.setScale(3);//for testing with assertEquals
		
	}


	

	
}
