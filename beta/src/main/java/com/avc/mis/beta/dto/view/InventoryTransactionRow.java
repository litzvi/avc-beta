/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class InventoryTransactionRow extends BasicDTO {

	private String poCodes;
	private String suppliers;
	private String processName;
	private String productionLine;
	private LocalDateTime receiptDate;
	private LocalDateTime transactionDate;
	private BasicValueEntity<Item> item;
	private AmountWithUnit amountSubtracted;
	private AmountWithUnit amountAdded;
	private ProcessStatus status;
	private String[] approvals;
	private String remarks;
	
	
	public InventoryTransactionRow(Integer id, String poCodes, String suppliers, 
			String processName, String productionLine, LocalDateTime receiptDate, LocalDateTime transactionDate,
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit,
			BigDecimal amountSubtracted, BigDecimal amountAdded,
			ProcessStatus status, String approvals, String remarks) {
		super(id);
		this.poCodes = poCodes;
		this.suppliers = suppliers;
		this.processName = processName;
		this.productionLine = productionLine;
		this.receiptDate = receiptDate;
		this.transactionDate = transactionDate;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		if(amountSubtracted != null)
			this.amountSubtracted = new AmountWithUnit(amountSubtracted, defaultMeasureUnit);
		if(amountAdded != null)
			this.amountAdded = new AmountWithUnit(amountAdded, defaultMeasureUnit);
		this.status = status;
		if(approvals == null || approvals.startsWith(":")) {
			this.approvals = null;
		}
		else {
			this.approvals = Stream.of(approvals.split(",")).toArray(String[]::new);
		}
		this.remarks = remarks;
	}
	
	


}
