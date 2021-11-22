/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Row in list of inventory transactions report.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public abstract class InventoryTransactionRow extends BasicDTO implements Comparable<InventoryTransactionRow> {

	private String poCodes;
	private String suppliers;
	private String processName;
	private String productionLine;
	private LocalDateTime receiptDate;
	private LocalDateTime transactionDate;
	private BasicValueEntity<Item> item;
	@JsonIgnore 
	private AmountWithUnit amount;
	private ProcessStatus status;
	private String remarks;
	
	
	public InventoryTransactionRow(Integer id, String poCodes, String suppliers, 
			String processName, String productionLine, LocalDateTime receiptDate, LocalDateTime transactionDate,
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit,
			BigDecimal amount,
			ProcessStatus status, String remarks) {
		super(id);
		this.poCodes = poCodes;
		this.suppliers = suppliers;
		this.processName = processName;
		this.productionLine = productionLine;
		this.receiptDate = receiptDate;
		this.transactionDate = transactionDate;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		if(amount != null)
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
		this.status = status;
		this.remarks = remarks;
	}
	
	@Override
	public int compareTo(@NonNull InventoryTransactionRow other) {
		int compareDate = getTransactionDate().compareTo(other.getTransactionDate());
		if(compareDate != 0) {
			return compareDate;
		}
		else {
			return getId().compareTo(other.getId());
		}
	}
	
	


}
