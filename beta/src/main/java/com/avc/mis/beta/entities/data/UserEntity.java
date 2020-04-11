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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ObjectDataEntity;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.entities.values.CompanyPosition;
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
@Entity
@Table(name = "USERS")
public class UserEntity extends ObjectDataEntity {
	
	@EqualsAndHashCode.Include
	@Id
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	@MapsId
	private Person person;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "personId"))
	@ElementCollection(targetClass = Role.class)
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Set<Role> roles = new HashSet<>();
	
	//should be only in staff
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "positionId")
//	private CompanyPosition position;

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
