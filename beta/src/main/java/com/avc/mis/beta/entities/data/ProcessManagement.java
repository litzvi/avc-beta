/**
 * 
 */
package com.avc.mis.beta.entities.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.ValueInterface;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESS_MANAGEMENT", indexes = {@Index(columnList = "processTypeId") }, 
		uniqueConstraints = { @UniqueConstraint(columnNames = {"processTypeId", "userId", "managementType" }) })
public class ProcessManagement extends LinkEntity implements ValueInterface {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processTypeId", nullable = false, updatable = false)
	@NotNull(message = "Process management needs to specify a process type", groups = OnPersist.class)
	private ProcessType processType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false, updatable = false)
	@NotNull(message = "Process management has to reference a user", groups = OnPersist.class)
	private UserEntity user;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Process management needs to specify the management type")
	private ManagementType managementType;
	
	@Override
	public String getValue() {
		return managementType.toString();
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@Override
	public void setReference(Object user) {
		this.setUser((UserEntity)user);
	}

}
