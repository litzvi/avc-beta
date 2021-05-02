/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.data.UserLogin;
import com.avc.mis.beta.dto.generic.ValueObject;
import com.avc.mis.beta.dto.view.UserRow;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.Role;

/**
 * @author Zvi
 *
 */
public interface UserRepository extends BaseRepository<UserEntity> {
	
	//org.springframework.data.jpa.repository.Query can't handle the set (roles) in select new
	@org.springframework.data.jdbc.repository.query.Query(
			"select new com.avc.mis.beta.dto.values.UserLogin(u.id, u.version, u.username, u.password, u.roles) "
			+ "from UserEntity u "
			+ "where u.username = ?1 "
				+ "and u.active = true")
	Optional<UserLogin> findByUsername(String username);
	
	/**
	 * @return table of all users information except their roles.
	 */
	@Query("select new com.avc.mis.beta.dto.view.UserRow(u.id, p.name, u.username, u.active) "
			+ "from UserEntity u "
				+ "join u.person p ")
	List<UserRow> findUserRowTable();
	
	/**
	 * @return (user id, role) pairs of joining all users with their corresponding role
	 */
	@Query("select new com.avc.mis.beta.dto.generic.ValueObject(u.id, r) "
			+ "from UserEntity u "
				+ "join u.roles r ")
	Stream<ValueObject<Role>> findAllRolesByUsers();

	@Query("select new com.avc.mis.beta.dto.basic.UserBasic(u.id, u.version, u.username) "
			+ "from UserEntity u "
			+ "where u.active = true")
	List<UserBasic> findAllBasic();
	
	@Query("select u from UserEntity u "
				+ "left join fetch u.person p "
					+ "left join fetch p.idCard idCard "
					+ "left join fetch p.contactDetails cd "
			+ "where (u.id = :id or :id is null) "
				+ "and (u.username = :username or :username is null) "
				+ "and u.active = true")
	Optional<UserEntity> findUserByIdOrUsername(Integer id, String username);

	@Query("select new java.lang.Boolean(count(*) > 0) "
			+ "from UserEntity u "
			+ "where u.username = :username "
				+ "and u.active = true ")
	Boolean containsUsername(String username);
}
