/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.Role;

/**
 * @author Zvi
 *
 */
public interface UserRepository extends Repository<UserEntity, Integer> {

	@Query("select new com.avc.mis.beta.dto.values.UserLogin(u.id,u.username, u.password, u.roles) "
			+ "from UserEntity u "
//				+ "join u.person p "
			+ "where u.username = ?1 "
				+ "and u.active = true")
	Optional<UserLogin> findByUsername(String username);

	@Query("select new com.avc.mis.beta.dto.data.UserDTO(u.id, u.version, p, u.username, u.password, u.roles) "
			+ "from UserEntity u "
				+ "left join u.person p "
			+ "left join p.idCard id "
			+ "left join p.contactDetails cd "
			+ "where u.username = ?1 "
				+ "and u.active = true")
	Optional<UserDTO> findUserByUsername(String username);

	@Query("select new com.avc.mis.beta.dto.data.UserDTO(u.id, u.version, p, u.username, u.password, u.roles) "
			+ "from UserEntity u "
				+ "left join u.person p "
			+ "left join p.idCard id "
			+ "left join p.contactDetails cd "
			+ "where u.id = ?1 "
				+ "and u.active = true")
	Optional<UserDTO> findById(Integer id);
}
