/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.basic.DataObjectWithName;
import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.basic.ValueObject;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.repositories.PersonRepository;
import com.avc.mis.beta.repositories.UserRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class UserReports {

	@Autowired private UserRepository userRepository;	
	@Autowired private PersonRepository personRepository;	

	/**
	 * USERS TABLE DISPLAY.
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
	 * PERSONS DROP DOWN
	 * Gets a list of existing people in the database, in basic form for referencing - id, version and name
	 * @return List of PersonBasic for all persons.
	 */
	@Transactional(readOnly = true)
	public List<DataObjectWithName<Person>> getPersonsBasic() {
		return getPersonRepository().findAllPersonsBasic();		
	}
	
	/**
	 * USERS DROP DOWN
	 * Gets a list of existing users in the database, in basic form for referencing - id, version and name
	 * @return List of UserBasic for all users.
	 */
	@Transactional(readOnly = true)
	public List<UserBasic> getUsersBasic() {
		return getUserRepository().findAllBasic();		
	}
	

	
}
