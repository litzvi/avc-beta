/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.avc.mis.beta.dto.values.UserLogin;

/**
 * Utility for finding the current logged in user.
 * 
 * @author Zvi
 *
 */
@Component
public class UserAware {

	public Optional<UserLogin> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
	
		return Optional.of((UserLogin) authentication.getPrincipal());
	}
}
