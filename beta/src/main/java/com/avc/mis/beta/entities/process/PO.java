/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PURCHASE_ORDERS")
@PrimaryKeyJoinColumn(name = "processId")
public class PO extends ProductionProcess {
	
//	@EqualsAndHashCode.Include
//	@Id
//	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "com.avc.mis.beta.utilities.UseExistingIdOtherwiseGenerateUsingIdentity")
//	@GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
//	@Column(nullable = false, updatable = false)
//	private Integer id;
	
//	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
//	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
//	@JoinColumn(name = "processId", updatable = false, nullable = false)
//	private ProductionProcess orderProcess;
	
//	@ManyToOne
//	@JoinColumn(name = "contractTypeId", updatable = false, nullable = false)
//	private ContractType contractType;
	
	@ManyToOne 
	@JoinColumn(name = "supplierId", updatable = false, nullable = false)
	private Supplier supplier; 
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus = OrderStatus.OPEN_PENDING;
	
//	@ToString.Exclude
//	@OneToMany(mappedBy = "po", fetch = FetchType.LAZY)
//	@BatchSize(size = DAO.BATCH_SIZE)
//	private Set<ProductionProcess> processes = new HashSet<>();
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "po", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<OrderItem> orderItems = new HashSet<>();
	


	/**
	 * @return the orderProcess
	 */
//	public ProductionProcess getOrderProcess() {
//		if(this.orderProcess == null) {
//			setOrderProcess(new ProductionProcess());
//		}
//		return this.orderProcess;
//	}

	/**
	 * @param orderProcess the orderProcess to set
	 */
//	public void setOrderProcess(ProductionProcess orderProcess) {
//		if(orderProcess != null) {
//			orderProcess.setReference(this);
//			this.orderProcess = orderProcess;			
//		}
//	}

	/**
	 * @return the orderItems
	 */
	public OrderItem[] getOrderItems() {
		return (OrderItem[])this.orderItems.toArray(new OrderItem[this.orderItems.size()]);
	}

	/**
	 * @param orderItems the orderItems to set
	 */
	public void setOrderItems(OrderItem[] orderItems) {
		this.orderItems = Insertable.filterAndSetReference(orderItems, (t) -> {t.setReference(this);	return t;});
	}

	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return super.isLegal() && this.supplier != null && this.orderItems.size() > 0;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Purchase Order is not legal, "
				+ "has to have a supplier and at least one order line";
	}
	
	
}
