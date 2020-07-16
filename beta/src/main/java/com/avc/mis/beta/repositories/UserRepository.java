/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.report.UserRow;
import com.avc.mis.beta.dto.values.UserBasic;
import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.dto.values.ValueObject;
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

	@Query("select new com.avc.mis.beta.dto.values.UserBasic(u.id, u.version, u.username) "
			+ "from UserEntity u "
			+ "where u.active = true")
	List<UserBasic> findAllBasic();

//	@Query("update UserEntity u "
//			+ "set u.password = :newPassword "
//			+ "where u.id := userId and u.password = :oldPassword")
//	void changePassword(Integer userId, String oldPassword, String newPassword);
	
	@Query("select new com.avc.mis.beta.dto.report.UserRow(u.id, p.name, u.username, u.active) "
			+ "from UserEntity u "
				+ "join u.person p "
			+ "where u.active = true")
	List<UserRow> findUserRowTable();
	
//	@org.springframework.data.jdbc.repository.query.Query(
//			"select new com.avc.mis.beta.dto.values.UserRow(u.id, p.name, u.username, u.roles, u.active) "
//			+ "from UserEntity u "
//				+ "join u.person p "
//			+ "where u.active = true")
//	List<UserRow> findUserRowTable();
	
	@Query("select new com.avc.mis.beta.dto.values.ValueObject(u.id, r) "
			+ "from UserEntity u "
				+ "join u.roles r "
			+ "where u.active = true")
	Stream<ValueObject<Role>> findAllRolesByUsers();

	@Query("select new java.lang.Boolean(count(*) > 0) "
			+ "from UserEntity u "
			+ "where u.username = :username "
				+ "and u.active = true ")
	Boolean containsUsername(String username);
}
