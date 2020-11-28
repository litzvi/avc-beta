/**
 * 
 */
package com.avc.mis.beta.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.data.UserLogin;
import com.avc.mis.beta.repositories.UserRepository;

/**
 * @author Zvi
 *
 */
@Service
public class UserDetailsServiceImp implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserLogin> user = userRepository.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
		return user.get();
	}

}
