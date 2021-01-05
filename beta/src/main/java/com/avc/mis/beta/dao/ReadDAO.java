/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.avc.mis.beta.dto.data.UserLogin;
import com.avc.mis.beta.utilities.UserAware;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Abstract root base class for all data access objects.
 * 
 * @author Zvi
 *
 */
@Getter(value = AccessLevel.PRIVATE)
public abstract class ReadDAO {
	
	@Autowired UserAware userAware;

	/**
	 * Gets the logged in user id.
	 * @return the id of currently logged in user.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public Integer getCurrentUserId() {
		Optional<UserLogin> userEntity = getUserAware().getCurrentUser();
		userEntity.orElseThrow(() -> new IllegalStateException("No user logged in or user not reachable"));
		return userEntity.get().getId();
	}
	
	UserLogin getCurrentUser() {
		return getUserAware().getCurrentUser().orElseThrow(() -> new IllegalStateException("No user logged in or user not reachable"));
	}

	

}
