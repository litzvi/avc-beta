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
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessAlert;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ApprovalType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.entities.process.UserMessage;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
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
	
	private static Integer SERIAL_NO = 1937;
	
	@Autowired private Users users;
	
	@Autowired private ObjectTablesReader objectTableReader;
	
	@Autowired private ProcessInfoWriter processInfoWriter;
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private ProcessInfoReader processDisplay;
	
	@Autowired private UserDetailsServiceImp userDetailsServiceImp;
	
	@Autowired private ProcessInfoRepository processRepository;
	

	
	@Test
	void usersTest() {

		//insert and edit a user
		UserEntity user = new UserEntity();
		user.setUsername("isral" + SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		UserLogin expected;
		expected = new UserLogin(user);
		users.addUser(user);
		Person p = user.getPerson();
		p.setName("isssssssssral" + SERIAL_NO);
		users.editPersonalDetails(user);
		UserLogin actual = (UserLogin) userDetailsServiceImp.loadUserByUsername(user.getUsername());
		if(expected.getPassword().equals(actual.getPassword())) {
			fail("Should fail on passwords comparison");
		} 
		expected.setPassword(null);
		actual.setPassword(null);
		assertEquals(expected, actual, "failed test of adding user");
		users.permenentlyRemoveUser(user.getId());
		users.permenentlyRemovePerson(user.getPerson().getId());
		
		//insert and edit user with 2 roles
		user = new UserEntity();
		user.setUsername("zvi" + SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		user.getRoles().add(Role.ROLE_MANAGER);
		users.addUser(user);
		user.setPassword("password");
		user.getRoles().clear();
		expected = new UserLogin(user);
		users.editUser(user);
		actual = (UserLogin) userDetailsServiceImp.loadUserByUsername(user.getUsername());
		expected.setPassword(null);
		actual.setPassword(null);
		assertEquals(expected, actual, "failed test of adding user");
		users.permenentlyRemoveUser(user.getId());
		users.permenentlyRemovePerson(user.getPerson().getId());
		
		//open user for existing person and then edit password
		user = new UserEntity();
		user.setUsername("eli" + SERIAL_NO);
		user.setPassword("309");
		user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
		user.getRoles().add(Role.ROLE_MANAGER);
		List<Person> persons = objectTableReader.getAllPersons();
		if(persons.isEmpty())
			fail("No persons for running test of adding user to existing person");
//		Person person = new Person();
//		person.setId(10);
		user.setPerson(persons.get(0));
		users.openUserForPerson(user);
		UserDTO userByusername = users.getUserByUsername("eli" + SERIAL_NO);
		UserDTO userById = users.getUserById(user.getId());
		assertEquals(userByusername, userById, "Failed test fetching user by id vs. by username");
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
				dao.getProcessTypeByValue(ProcessName.CASHEW_ORDER), ApprovalType.REQUIRED_APPROVAL);
		ProcessAlert processAlert = processDisplay.getProcessTypeAlert(processAlertId);
		processInfoWriter.editProcessTypeAlert(processAlert, ApprovalType.REVIEW);
		processInfoWriter.removeProcessTypeAlert(processAlertId);
		processInfoWriter.removeUserMessages(user.getId());
		users.permenentlyRemoveUser(user.getId());

		
	}
}
