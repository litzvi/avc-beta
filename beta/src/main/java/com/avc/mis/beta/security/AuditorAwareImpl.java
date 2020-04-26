/**
 * 
 */
package com.avc.mis.beta.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.utilities.UserAware;

/**
 * @author Zvi
 *
 */
public class AuditorAwareImpl implements AuditorAware<UserEntity> {
	
	@Autowired UserAware userAware;

	@Override
	public Optional<UserEntity> getCurrentAuditor() {

		Optional<UserEntity> user = userAware.getCurrentUser()
				.map(u -> {
					UserEntity userEntity = new UserEntity();
					userEntity.setId(u.getId());
					userEntity.setVersion(u.getVersion());
					return userEntity;
				});
		
		
		return user;
	}
}
