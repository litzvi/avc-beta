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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.avc.mis.beta.entities.BaseEntityNoId;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.services.DAO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PURCHASE_ORDERS")
//@NamedQuery(name = "PO.findAll", 
//	query = "select po from PO po "
//		+ "left join fetch po.orderProcess p ")
//@NamedQuery(name = "PO.findByOrderType", 
//	query = "select po from PO po "
//		+ "left join fetch po.orderProcess p "
//		+ "where p.processType = :type ")
//@NamedQuery(name = "PO.details", 
//	query = "select po from PO po "
//		+ "left join fetch po.orderProcess p "
//		+ "where po.id = :poid ")
public class PO extends BaseEntityNoId {
	
	@EqualsAndHashCode.Include
	@Id
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "com.avc.mis.beta.utilities.UseExistingIdOtherwiseGenerateUsingIdentity")
	@GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
	@Column(nullable = false, updatable = false)
	private Integer id;
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", updatable = false, nullable = false)
	private ProductionProcess orderProcess;
	
	@ManyToOne
	@JoinColumn(name = "contractTypeId", updatable = false, nullable = false)
	private ContractType contractType;
	
	@ManyToOne 
	@JoinColumn(name = "supplierId", updatable = false, nullable = false)
	private Supplier supplier; 
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status = OrderStatus.OPEN_PENDING;
	
//	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "po", fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<ProductionProcess> processes = new HashSet<>();
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "po", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<OrderItem> orderItems = new HashSet<>();
	


	/**
	 * @return the orderProcess
	 */
	public ProductionProcess getOrderProcess() {
		if(this.orderProcess == null) {
			setOrderProcess(new ProductionProcess());
		}
		return this.orderProcess;
	}

	/**
	 * @param orderProcess the orderProcess to set
	 */
	public void setOrderProcess(ProductionProcess orderProcess) {
		if(orderProcess != null) {
			orderProcess.setReference(this);
			this.orderProcess = orderProcess;			
		}
	}

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
	
	@Override
	public boolean isLegal() {
		return this.contractType != null && this.supplier != null && this.orderItems.size() > 0;
	}

	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal()) {
			throw new IllegalArgumentException("Purchase Order is not legal, "
					+ "has to have a supplier and at least one order line");
		}
		
	}
	
	
}
