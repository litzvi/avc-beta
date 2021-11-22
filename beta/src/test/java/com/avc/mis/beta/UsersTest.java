/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dto.data.PersonDTO;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.data.UserLogin;
import com.avc.mis.beta.dto.link.ProcessManagementDTO;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.repositories.PoProcessRepository;
import com.avc.mis.beta.security.UserDetailsServiceImp;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.Users;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithUserDetails("eli")
public class UsersTest {
	
	private static Integer SERIAL_NO = 1977;
	
	@Autowired private Users users;
	
	@Autowired private ObjectTablesReader objectTableReader;
	
	@Autowired private ProcessInfoWriter processInfoWriter;
	
//	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private ProcessInfoReader processDisplay;
	
	@Autowired private UserDetailsServiceImp userDetailsServiceImp;
	
	@Autowired private PoProcessRepository processRepository;
	
	@Autowired private DeletableDAO deletableDAO;

	
	@Test
	void usersTest() {

		//insert and edit a user
		UserDTO user = new UserDTO();
		user.setUsername("isral" + SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		UserLogin expected;
		expected = new UserLogin(user);
		Integer userId = users.addUser(user);
		user = users.getUserById(userId);
		PersonDTO p = user.getPerson();
		p.setName("isssssssssral" + SERIAL_NO);
		users.editPersonalDetails(user);
		UserLogin actual = (UserLogin) userDetailsServiceImp.loadUserByUsername(user.getUsername());
		if(expected.getPassword().equals(actual.getPassword())) {
			fail("Should fail on passwords comparison");
		} 
		expected.setPassword(null);
		actual.setPassword(null);
		assertEquals(expected, actual, "failed test of adding user");
		users.permenentlyRemoveUser(userId);
		users.permenentlyRemovePerson(user.getPerson().getId());
		
		//insert and edit user with 2 roles
		user = new UserDTO();
		user.setUsername("zvi" + SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		user.getRoles().add(Role.ROLE_MANAGER);
		userId = users.addUser(user);
		user = users.getUserById(userId);
		user.setPassword("password");
		user.getRoles().clear();
		expected = new UserLogin(user);
		users.editUser(user);
		actual = (UserLogin) userDetailsServiceImp.loadUserByUsername(user.getUsername());
		expected.setPassword(null);
		actual.setPassword(null);
		assertEquals(expected, actual, "failed test of adding user");
		users.permenentlyRemoveUser(userId);
		if(user.getPerson() != null)
			users.permenentlyRemovePerson(user.getPerson().getId());
		
		//open user for existing person and then edit password
		user = new UserDTO();
		user.setUsername("eli" + SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		user.getRoles().add(Role.ROLE_MANAGER);
		List<PersonDTO> persons = objectTableReader.getAllPersons();
		if(persons.isEmpty())
			fail("No persons for running test of adding user to existing person");
//		Person person = new Person();
//		person.setId(10);
		user.setPerson(persons.get(0));
		userId = users.openUserForPerson(user);
		UserDTO userByusername = users.getUserByUsername("eli" + SERIAL_NO);
		UserDTO userById = users.getUserById(userId);
		assertEquals(userByusername, userById, "Failed test fetching user by id vs. by username");
		user = userById;
		user.setPassword("password");
		user.getRoles().clear();
		expected = new UserLogin(user);
		users.editUser(user);
		actual = new UserLogin(user);
		actual.setPassword(null);
		expected.setPassword(null);
		assertEquals(expected, actual, "Failed test inserting user for existing person");
		
		//add, edit, remove processTypeAlert
		Integer processAlertId = processInfoWriter.addProcessTypeAlert(user.getId(), 
				ProcessName.CASHEW_ORDER, ManagementType.APPROVAL);
		ProcessManagementDTO processAlert = processDisplay.getProcessTypeAlert(processAlertId);
//		processInfoWriter.editProcessTypeAlert(processAlert.getId(), ManagementType.REVIEW);
		processInfoWriter.removeProcessTypeAlert(processAlertId);
		processInfoWriter.removeUserMessages(userId);
		
		users.changePassword("309", "123");
		users.changePassword("309", "309");
		users.permenentlyRemoveUser(user.getId());
		
		
	}
}
