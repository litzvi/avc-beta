/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "RECEIPT_ITEMS")
@PrimaryKeyJoinColumn(name = "processItemId")
public class ReceiptItem extends ProcessItem {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderItemId")
	private OrderItem orderItem;
	
	@Column(nullable = false, precision = 19, scale = 3)
	private BigDecimal extraRequested;	
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
}
