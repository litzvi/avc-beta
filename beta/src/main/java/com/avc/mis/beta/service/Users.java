/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.SoftDeletableDAO;
import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.basic.ValueObject;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.Role;
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
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(rollbackFor = Throwable.class)
public class Users {
	
	@Autowired private SoftDeletableDAO dao;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	@Autowired private UserRepository userRepository;	
	@Autowired private PersonRepository personRepository;	
	@Autowired private PasswordEncoder encoder;
	
	/**
	 * Gets the table of all system users
	 * @return List of UserRow - id, username, password, roles and if the user is active.
	 */
	@Transactional(readOnly = true)
	public List<UserRow> getUsersTable() {
		List<UserRow> userRows = getUserRepository().findUserRowTable();
		Stream<ValueObject<Role>> roles = getUserRepository().findAllRolesByUsers();
		Map<Integer, Set<Role>> rolesMap = roles
				.collect(Collectors.groupingBy(ValueObject::getId, 
						Collectors.mapping(ValueObject::getValue, Collectors.toSet())));
		userRows.forEach(r -> r.setRoles(rolesMap.get(r.getId())));
		return userRows;		
	}
	
	/**
	 * Gets a list of existing people in the database, in basic form for referencing - id, version and name
	 * @return List of PersonBasic for all persons.
	 */
	@Transactional(readOnly = true)
	public List<DataObjectWithName> getPersonsBasic() {
		return getPersonRepository().findAllPersonsBasic();		
	}
	
	/**
	 * Gets a list of existing users in the database, in basic form for referencing - id, version and name
	 * @return List of UserBasic for all users.
	 */
	@Transactional(readOnly = true)
	public List<UserBasic> getUsersBasic() {
		return getUserRepository().findAllBasic();		
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
		dao.addEntity(user.getPerson());
//		String generatedPass = RandomStringUtils.random(8, true, true);
		//perhaps send email
		user.setPassword(encoder.encode(user.getPassword()));
		dao.addEntity(user);
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
		dao.addEntity(user, user.getPerson());
	}
	
	/**
	 * Find user with full details with given username - including id card and contact details if exist.
	 * @param username of the user to find
	 * @return UserDTO with full details 
	 * @throws IllegalArgumentException if there is no User with given username.
	 */
	@Transactional(readOnly = true)
	public UserDTO getUserByUsername(String username) {
		Optional<UserEntity> user = getUserRepository().findUserByUsername(username);
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
		Optional<UserEntity> user = getUserRepository().findById(userId);
		user.orElseThrow(() -> new IllegalArgumentException("No User with given ID"));
		return new UserDTO(user.get());
	}
	
	/**
	 * @param string
	 * @return
	 */
	public boolean contains(String username) {
		return getUserRepository().containsUsername(username);
	}
	
	/**
	 * Sets the user as not active
	 * @param userId of UserEntity to be set
	 */
	public void removeUser(int userId) {
//		SoftDeleted entity = getEntityManager().getReference(UserEntity.class, userId);
		dao.removeEntity(UserEntity.class, userId);
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
	
	/**
	 * Edit the user details, dosen't edit the person details for this user.
	 * @param user to be edited
	 */
	public void editUser(UserEntity user) {
		dao.editEntity(user);
	}
	
//	public void changePassword(Integer userId, String oldPassword, String newPassword) {
//		userRepository.changePassword(userId, oldPassword, newPassword);
//	}
	
	/**
	 * Edits all the personal details of this user, will edit user and person details.
	 * @param user to be edited
	 */
	public void editPersonalDetails(UserEntity user) {
		Person person = user.getPerson();
		dao.editEntity(user);
		if(person != null)
			dao.editEntity(person);
		
	}

	
}
