/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.entities.data.UserEntity;

/**
 * @author Zvi
 *
 */
public interface UserRepository extends Repository<UserEntity, Integer> {

	@Query("select new com.avc.mis.beta.dto.values.UserLogin(u.id, u.version, u.username, u.password, u.roles) "
			+ "from UserEntity u "
			+ "where u.username = ?1 "
				+ "and u.active = true")
	Optional<UserLogin> findByUsername(String username);

	@Query("select u from UserEntity u "
				+ "left join fetch u.person p "
					+ "left join fetch p.idCard idCard "
					+ "left join fetch p.contactDetails cd "
			+ "where u.username = :username "
				+ "and u.active = true")
	Optional<UserEntity> findUserByUsername(String username);

	@Query("select u from UserEntity u "
				+ "left join fetch u.person p "
					+ "left join fetch p.idCard idCard "
					+ "left join fetch p.contactDetails cd "
			+ "where u.id = :id "
				+ "and u.active = true")
	Optional<UserEntity> findById(Integer id);
}
