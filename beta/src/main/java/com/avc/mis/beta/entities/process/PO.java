/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.processinfo.OrderItem;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Purchase Order with order items.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PURCHASE_ORDERS")
@PrimaryKeyJoinColumn(name = "processId")
public class PO extends PoProcess {
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "po", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.ALL, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	@NotEmpty(message = "Purchase Order has to have at least one order line")
	private Set<OrderItem> orderItems = new HashSet<>();
	
	private String personInCharge;
	
	/**
	 * Gets the list of Items as an array (can be ordered).
	 * @return the orderItems
	 */
	public OrderItem[] getOrderItems() {
		return (OrderItem[])this.orderItems.toArray(new OrderItem[this.orderItems.size()]);
	}

	/**
	 * Setter for adding order items to order, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param orderItems the orderItems to set
	 */
	public void setOrderItems(OrderItem[] orderItems) {
		Ordinal.setOrdinals(orderItems);
		this.orderItems = Insertable.setReferences(orderItems, (t) -> {t.setReference(this);	return t;});
	}

	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
//	@JsonIgnore
//	@Override
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Purchase Order";
	}
	
}
