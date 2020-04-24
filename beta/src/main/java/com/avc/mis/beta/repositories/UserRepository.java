/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.values.PersonBasic;
import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.dto.values.UserRow;
import com.avc.mis.beta.entities.data.UserEntity;

/**
 * @author Zvi
 *
 */
public interface UserRepository extends BaseRepository<UserEntity> {

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

//	@Query("select u from UserEntity u ")
	List<UserEntity> findAll();

	@Query("select new com.avc.mis.beta.dto.values.PersonBasic(p.id, p.version, p.name) "
			+ "from Person p "
			+ "where p.active = true")
	List<PersonBasic> findAllPersonsBasic();
	
//	@Query("select new com.avc.mis.beta.dto.values.UserRow(u.id, p.name, u.username, u.roles, u.active) "
//			+ "from UserEntity u "
//				+ "left join u.person p")
//	List<UserRow> findUserRowTable();
}
