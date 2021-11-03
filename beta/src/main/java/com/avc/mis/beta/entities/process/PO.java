/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Purchase Order with order items.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "PURCHASE_ORDERS")
@PrimaryKeyJoinColumn(name = "processId")
public class PO extends PoProcess {
	
	@JsonIgnore
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = false, updatable = false, columnDefinition = "boolean not null default 0")
	private boolean closed = false;
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "po", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.ALL, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	@NotEmpty(message = "Purchase Order has to have at least one order line")
	private Set<OrderItem> orderItems = new HashSet<>();
	
//	private String personInCharge;
	
	public void setGeneralPoCode(GeneralPoCode poCode) {
		super.setPoCode(poCode);
	}
	
	/**
	 * Gets the list of Items as an array (can be ordered).
	 * @return the orderItems
	 */
	public OrderItem[] getOrderItems() {
		OrderItem[] orderItems = (OrderItem[])this.orderItems.toArray(new OrderItem[this.orderItems.size()]);
		Arrays.sort(orderItems, Ordinal.ordinalComparator());
		return orderItems;
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
		
	@NotNull(message = "Purchase Order has to reference a po code")
	@Override
	public BasePoCode getPoCode() {
		return super.getPoCode();
	}
	
}
