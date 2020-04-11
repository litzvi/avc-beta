/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

import com.avc.mis.beta.dto.data.UserDTO;
import com.avc.mis.beta.entities.data.UserEntity;

/**
 * @author Zvi
 *
 */
public interface UserRepository extends Repository<UserEntity, Integer> {

	@Query("select new com.avc.mis.beta.dto.data.UserDTO(u.id, u.version, u.username, u.password, u.roles) "
			+ "from UserEntity u "
//				+ "join u.person p "
			+ "where u.username = ?1 "
				+ "and u.active = true")
	Optional<UserDTO> findByUsername(String username);
}
