/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.Phone;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.interfaces.Insertable;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PURCHASE_ORDERS")
public class PO implements Insertable {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", updatable = false, nullable = false)
	private ProductionProcess orderProcess;
	
	@ManyToOne 
	@JoinColumn(name = "contractTypeId", updatable = false, nullable = false)
	private ContractType contractType;
	
	@ManyToOne 
	@JoinColumn(name = "supplierId", updatable = false, nullable = false)
	private Supplier supplier; 
	
	@ManyToOne 
	@JoinColumn(name = "statusId")
	private OrderStatus status;
	
//	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "po", fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<ProductionProcess> processes = new HashSet<>();
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "po", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void prePersistOrUpdate() {
		// TODO Auto-generated method stub
		
	}
	
	
}
