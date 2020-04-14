/**
 * 
 */
package com.avc.mis.beta.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.entities.data.UserEntity;

/**
 * @author Zvi
 *
 */
public class AuditorAwareImpl implements AuditorAware<UserEntity> {

	@Override
	public Optional<UserEntity> getCurrentAuditor() {
//		UserEntity user = new UserEntity();
//		user.setId(1);
//		user.setVersion(0L);
//		
//		return Optional.of(user);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
	
		UserLogin userLogin = ((UserLogin) authentication.getPrincipal());
		UserEntity user = new UserEntity();
		user.setId(userLogin.getId());
		user.setVersion(userLogin.getVersion());
		
		return Optional.of(user);
	}
	

}
