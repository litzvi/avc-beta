/**
 * 
 */
package com.avc.mis.beta;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.avc.mis.beta.dao.Users;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.Role;

/**
 * @author Zvi
 *
 */
@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	Users users;
	
	@Override
	public void run(String... args) throws Exception {
		
		//insert SYSTEM MANAGER
		UserEntity user = new UserEntity();
		user.setUsername("isral309@gmail.com");
//		String generatedPass = RandomStringUtils.random(8, true, true);
		//perhaps send email
		user.setPassword("309");
		user.getRoles().add(Role.SYSTEM_MANAGER);
		users.addUser(user);
		
	}

}
