/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.values.PersonBasic;
import com.avc.mis.beta.dto.values.UserRow;
import com.avc.mis.beta.entities.SoftDeleted;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.repositories.PersonRepository;
import com.avc.mis.beta.repositories.UserRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 
 * 
 * @author Zvi
 *
 */
@Repository
@Getter(value = AccessLevel.PRIVATE)
@Transactional(rollbackFor = Throwable.class)
public class Users extends SoftDeletableDAO {
	
	@Autowired private UserRepository userRepository;
	
	@Autowired private PersonRepository personRepository;
	
	@Autowired private PasswordEncoder encoder;
	
	/**
	 * Gets the table of all system users
	 * @return List of UserRow - id, username, password, roles and if the user is active.
	 */
	@Transactional(readOnly = true)
	public List<UserRow> getUsersTable() {
//		List<UserRow> userRows = getUserRepository().findUserRowTable();
//		List<UserRow> userRows = new ArrayList<>();
		List<UserRow> userRows = this.userRepository.findAll().map(u -> new UserRow(u)).collect(Collectors.toList());
		return userRows;		
	}
	
	/**
	 * Gets a list of existing people in the database, in basic form for referencing - id, version and name
	 * @return List of PersonBasic for all persons.
	 */
	@Transactional(readOnly = true)
	public List<PersonBasic> getPersonsBasic() {
		return this.personRepository.findAllPersonsBasic();		
	}
	
	/**
	 * Adds a new login user not connected to existing person.
	 * Might have or not have the person's details.
	 * @param user UserEntity with person field null.
	 */
	public void addUser(UserEntity user) {
		Person person = user.getPerson();
		if(person == null) {
			person = new Person();
			person.setName(user.getUsername());
		}		
		user.setPerson(person);
		addEntity(user.getPerson());
//		String generatedPass = RandomStringUtils.random(8, true, true);
		//perhaps send email
		user.setPassword(encoder.encode(user.getPassword()));
		addEntity(user);
	}
	
	/**
	 * Opening a login user account for an existing person
	 * @param user UserEntity referencing an existing person
	 */
	public void openUserForPerson(UserEntity user) {
		if(user.getPerson() == null) {
			throw new IllegalArgumentException("Trying to open user without existing person. See addUser(user) method.");
		}
//		String generatedPass = RandomStringUtils.random(8, true, true);
		//perhaps send email
		user.setPassword(encoder.encode(user.getPassword()));
		addEntity(user, user.getPerson());
	}
	
	/**
	 * Find user with full details with given username - including id card and contact details if exist.
	 * @param username of the user to find
	 * @return UserDTO with full details 
	 * @throws IllegalArgumentException if there is no User with given username.
	 */
	@Transactional(readOnly = true)
	public UserDTO getUserByUsername(String username) {
		Optional<UserEntity> user = userRepository.findUserByUsername(username);
		user.orElseThrow(() -> new IllegalArgumentException("No User with given username"));
		return new UserDTO(user.get());
	}
	
	/**
	 * Find user with full details with given id - including id card and contact details if exist.
	 * @param userId of the user to find
	 * @return UserDTO with full details 
	 * @throws IllegalArgumentException if there is no User with given id.
	 */
	@Transactional(readOnly = true)
	public UserDTO getUserById(Integer userId) {
		Optional<UserEntity> user = userRepository.findById(userId);
		user.orElseThrow(() -> new IllegalArgumentException("No User with given ID"));
		return new UserDTO(user.get());
	}
	
	/**
	 * Sets the user as not active
	 * @param userId of UserEntity to be set
	 */
	public void removeUser(int userId) {
		SoftDeleted entity = getEntityManager().getReference(UserEntity.class, userId);
		removeEntity(entity);
	}
	
	/**
	 * For testing only
	 * @param personId
	 */
	@Deprecated
	public void permenentlyRemoveUser(int personId) {
		permenentlyRemoveEntity(UserEntity.class, personId);
	}
	
	/**
	 * Edit the user details, dosen't edit the person details for this user.
	 * @param user to be edited
	 */
	public void editUser(UserEntity user) {
		editEntity(user);
	}
	
	public void changePassword(Integer userId, String oldPassword, String newPassword) {
		userRepository.changePassword(userId, oldPassword, newPassword);
	}
	
	/**
	 * Edits all the personal details of this user, will edit user and person details.
	 * @param user to be edited
	 */
	public void editPersonalDetails(UserEntity user) {
		Person person = user.getPerson();
		editEntity(user);
		if(person != null)
			editEntity(person);
		
	}
}
