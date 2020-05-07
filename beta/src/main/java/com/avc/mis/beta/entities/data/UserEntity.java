/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.avc.mis.beta.entities.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@BatchSize(size = BaseEntity.BATCH_SIZE)
@Entity
@Table(name = "USERS")
public class UserEntity extends ObjectEntityWithId {
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "personId", updatable = false)
	private Person person;
	
	@Column(nullable = false, unique = true, updatable = false)
	private String username;
	
	@Column(nullable = false, updatable = false)
	private String password;
	
	@CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "userId"))
	@ElementCollection(targetClass = Role.class)
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	@BatchSize(size = BaseEntity.BATCH_SIZE)
	private Set<Role> roles = new HashSet<>();
	
	//should be only in staff
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "positionId")
//	private CompanyPosition position;
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@Override
	public void setReference(Object person) {
		this.setPerson((Person)person);
	}

	@Override
	public boolean isLegal() {
		return this.person != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "UserEntity has to reference a person";
	}
}
