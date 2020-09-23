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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.avc.mis.beta.entities.enums.Role;
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
@Table(name = "USERS")
public class UserEntity extends ObjectEntityWithId {
	
	@Column(unique = true, nullable = false, updatable = false)
	@NotBlank(message = "Username is mandatory", groups = OnPersist.class)
	private String username;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId", updatable = false, nullable = false)
	@NotNull(message = "User has to reference a person", groups = OnPersist.class)
	private Person person;
	
	@Column(nullable = false, updatable = false)
	@NotBlank(message = "Password is mandatory", groups = OnPersist.class)
	private String password;
	
	@CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "userId"))
	@ElementCollection(targetClass = Role.class)
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Set<Role> roles = new HashSet<>();

//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
	@Override
	public void setReference(Object person) {
		this.setPerson((Person)person);
	}

}
