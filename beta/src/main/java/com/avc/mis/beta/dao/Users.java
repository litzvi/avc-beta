/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.entities.data.Person;
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
	
	/**
	 * Adds a new login user not connected to existing person.
	 * @param user UserEntity with person field null.
	 */
	public void addUser(UserEntity user) {
		Person person = new Person();
		person.setName(user.getUsername());
		user.setPerson(person);
		addEntity(user.getPerson());		
		openUserForPerson(user);
	}
	
	/**
	 * Opening a login user account for an existing person
	 * @param user UserEntity referencing an existing person
	 */
	public void openUserForPerson(UserEntity user) {
		//check that person exists
		user.setPassword(encoder.encode(user.getPassword()));
		addEntity(user);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserLogin loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserLogin> user = userRepository.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
		return user.get();
	}
}
