/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.Role;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserRow extends ValueDTO {
	
	
	String personName;
	String username;
	String value;
	String password; //might be removed
	@NonFinal Set<Role> roles;
	boolean active;
	
	public UserRow(Integer id, String personName, String username, Collection<Role> roles, Boolean active) {
		super(id);
		this.personName = personName;
		this.username = username;
		this.value = username;
		this.password = null;
		this.roles = roles.stream().collect(Collectors.toSet());
		this.active = active;
	}
	
	public UserRow(Integer id, String personName, String username, Boolean active) {
		super(id);
		this.personName = personName;
		this.username = username;
		this.value = username;
		this.password = null;
		this.active = active;
	}
	
	
	public UserRow(@NonNull UserEntity user) {
		super(user.getId());
		this.personName = user.getPerson().getName();
		this.username = user.getUsername();
		this.value = user.getUsername();
		this.password = null;
		this.roles = user.getRoles().stream().collect(Collectors.toSet());
		this.active = user.isActive();
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
