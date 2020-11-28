/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.avc.mis.beta.dto.DataDTO;
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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserLogin extends DataDTO implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private List<GrantedAuthority> authorities;
	
	public UserLogin(Integer id, Integer version, String username, String password, Collection<Role> roles) {
		super(id, version);
		this.username = username;
		this.password = password;
		authorities = roles.stream().map(u->new SimpleGrantedAuthority(u.name())).collect(Collectors.toList());
	}
	
	public UserLogin(@NonNull UserEntity user) {
		super(user.getId(), user.getVersion());
		this.username = user.getUsername();
		this.password = user.getPassword();
		authorities = user.getRoles().stream().map(u->new SimpleGrantedAuthority(u.name())).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
