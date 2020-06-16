/**
 * 
 */
package com.avc.mis.beta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.service.Users;

/**
 * @author Zvi
 *
 */
@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	Users users;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	
		//insert an initial SYSTEM_MANAGER in order to have application access on startup
		List<String> names = args.getOptionValues("n");
		List<String> passwords = args.getOptionValues("p");
		if(names != null && !names.isEmpty() && passwords != null && !passwords.isEmpty()) {
			UserEntity user = new UserEntity();
			user.setUsername(names.get(0));
			user.setPassword(passwords.get(0));
			user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
			users.addUser(user);
		}
		
		//should fill ProcessTypes, UOM and ContractTyoes
	}

}
