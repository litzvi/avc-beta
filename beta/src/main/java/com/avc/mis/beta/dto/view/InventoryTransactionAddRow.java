/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class InventoryTransactionAddRow extends InventoryTransactionRow {

	public InventoryTransactionAddRow(Integer id, String poCodes, String suppliers, String processName,
			String productionLine, LocalDateTime receiptDate, LocalDateTime transactionDate, Integer itemId,
			String itemValue, MeasureUnit defaultMeasureUnit, BigDecimal amount, ProcessStatus status, String remarks) {
		super(id, poCodes, suppliers, processName, productionLine, receiptDate, transactionDate, itemId, itemValue,
				defaultMeasureUnit, amount, status, remarks);
	}

	public AmountWithUnit getAmountAdded() {
		return getAmount();
	}
}
