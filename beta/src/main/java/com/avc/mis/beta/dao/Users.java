/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.repositories.UserRepository;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class Users extends SoftDeletableDAO implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	private PasswordEncoder encoder;
	
	public void addUser(UserEntity user) {
		user.setPassword(encoder.encode(user.getPassword()));
//		if()
		addEntity(user);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserDTO> user = userRepository.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
		return user.get();
	}
}
