/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.Role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDTO extends DataDTO {
	
	private PersonDTO person;
	private String username;
	//needed for filling in the password - never sending
	private String password = null; 
	private Set<String> authorities;
	
	public UserDTO(Integer id, Integer version, Person person, String username, Collection<Role> roles) {
		super(id, version);
		this.person = new PersonDTO(person);
		this.username = username;
//		this.password = password;
		authorities = roles.stream().map(u->u.name()).collect(Collectors.toSet());
	}
	
	public UserDTO(@NonNull UserEntity user) {
		super(user.getId(), user.getVersion());
		this.person = new PersonDTO(user.getPerson());
		this.username = user.getUsername();
//		this.password = user.getPassword();
		authorities = user.getRoles().stream().map(u->u.name()).collect(Collectors.toSet());
	}
}
