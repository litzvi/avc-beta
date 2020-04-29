/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.Role;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserRow extends ValueDTO {
	
	String personName;
	String username;
	String password; //might be removed
	Set<Role> roles;
	boolean active;
	
	public UserRow(Integer id, String personName, String username, Set<Role> roles, Boolean active) {
		super(id);
		this.personName = personName;
		this.username = username;
		this.password = null;
		this.roles = roles;
		this.active = active;
	}
	
	
	public UserRow(@NonNull UserEntity user) {
		super(user.getId());
		this.personName = user.getPerson().getName();
		this.username = user.getUsername();
		this.password = null;
		this.roles = user.getRoles().stream().collect(Collectors.toSet());
		this.active = user.isActive();
	}

	
}
