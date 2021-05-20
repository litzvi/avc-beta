/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.SoftDeletableDAO;
import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.repositories.UserRepository;
import com.avc.mis.beta.service.report.UserReports;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(rollbackFor = Throwable.class)
public class Users {
	
	@Autowired private SoftDeletableDAO dao;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	@Autowired private UserRepository userRepository;	
	@Autowired private PasswordEncoder encoder;
	
	/**
	 * ADD NEW USER - WITH ADDING PERSON (name = username)
	 * Adds a new login user not connected to existing person.
	 * Might have or not have the person's details.
	 * @param user UserEntity with person field null.
	 */
	public void addUser(UserEntity user) {
		Person person = user.getPerson();
		if(person == null) {
			person = new Person();
			person.setName(user.getUsername());
			user.setPerson(person);
		}		
		dao.addEntity(person);
		openUserForPerson(user);
	}
	
	/**
	 * ADD NEW USER - FOR EXISTING PERSON
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
		dao.addEntity(user, user.getPerson());
	}
	
	/**
	 * GET USER FULL DETAILS - BY USERNAME
	 * Find user with full details with given username - including id card and contact details if exist.
	 * @param username of the user to find
	 * @return UserDTO with full details 
	 * @throws IllegalArgumentException if there is no User with given username.
	 */
	@Transactional(readOnly = true)
	public UserDTO getUserByUsername(String username) {
		Optional<UserEntity> user = getUserRepository().findUserByIdOrUsername(null, username);
		return new UserDTO(user.orElseThrow(
				() -> new IllegalArgumentException("No User with given username")));
	}
	
	/**
	 * GET USER FULL DETAILS - BY USER ID
	 * Find user with full details with given id - including id card and contact details if exist.
	 * @param userId of the user to find
	 * @return UserDTO with full details 
	 * @throws IllegalArgumentException if there is no User with given id.
	 */
	@Transactional(readOnly = true)
	public UserDTO getUserById(Integer userId) {
		Optional<UserEntity> user = getUserRepository().findUserByIdOrUsername(userId, null);
		return new UserDTO(user.orElseThrow(
				() -> new IllegalArgumentException("No User with given ID")));
	}
	
	/**
	 * CHECK IF USER WAS ALREADY ADDED - BY USERNAME
	 * Check if there is a user with the given username.
	 * @param string the username
	 * @return true if there is a User with the given username, otherwise false.
	 */
	public boolean contains(String username) {
		return getUserRepository().containsUsername(username);
	}
	
	/**
	 * SOFT DELETE USER
	 * Sets the user as not active
	 * @param userId of UserEntity to be set
	 */
	public void removeUser(int userId) {
		dao.removeEntity(UserEntity.class, userId);
	}
	
	/**
	 * EDIT USER (DOES NOT EDIT PERSON)
	 * Edit the user details, dosen't edit the person details for this user.
	 * @param user to be edited
	 */
	public void editUser(UserEntity user) {
		user.setPerson(null); // not editing person
		dao.editEntity(user);
	}
	
	/**
	 * EDIT USER (EDITS CONTAINED PERSON AS WELL)
	 * Edits all the personal details of this user, will edit user and person details.
	 * @param user to be edited
	 */
	public void editPersonalDetails(UserEntity user) {
		Person person = user.getPerson();
		dao.editEntity(user);
		if(person != null)
			dao.editEntity(person);
		
	}
	
	/**
	 * CHANGE CURRENT USER'S PASSWORD
	 * Changes password for current user - from current to new - given both the current and new passwords.
	 * @param password the current password
	 * @param newPassword the new password
	 */
	public void changePassword(String password, String newPassword) {
		getDao().changeUserPassword(password, newPassword);			
		
	}

	/**
	 * For testing only
	 * @param userId
	 */
	@Deprecated
	public void permenentlyRemoveUser(int userId) {
		getDeletableDAO().permenentlyRemoveEntity(UserEntity.class, userId);
	}
	
	/**
	 * For testing only
	 * @param personId
	 */
	@Deprecated
	public void permenentlyRemovePerson(int personId) {
		getDeletableDAO().permenentlyRemoveEntity(Person.class, personId);
	}	

	//----------------------------Duplicate in UserReports - Should remove------------------------------------------

	@Autowired private UserReports userReports;	

	@Transactional(readOnly = true)
	public List<UserRow> getUsersTable() {
		return getUserReports().getUsersTable();		
	}
	
	@Transactional(readOnly = true)
	public List<DataObjectWithName<Person>> getPersonsBasic() {
		return getUserReports().getPersonsBasic();		
	}
	
	@Transactional(readOnly = true)
	public List<UserBasic> getUsersBasic() {
		return getUserReports().getUsersBasic();		
	}
	
}
