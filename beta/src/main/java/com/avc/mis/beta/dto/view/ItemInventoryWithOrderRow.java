/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.util.List;

import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ItemInventoryWithOrderRow extends ItemInventoryRow {

	private AmountWithUnit totalOrderBalance;

	private List<PoItemRow> orderItemRows;


	public ItemInventoryWithOrderRow(@NonNull ItemWithUnitDTO item) {
		super(item);
	}
	
	public void setOrderItemRows(List<PoItemRow> orderItemRows) {
		this.orderItemRows = orderItemRows;
		
		if(orderItemRows != null) {
			this.totalOrderBalance = orderItemRows.stream()
					.map(oi -> oi.getBalance())
					.reduce(AmountWithUnit::add).get();
		}
	}

}
