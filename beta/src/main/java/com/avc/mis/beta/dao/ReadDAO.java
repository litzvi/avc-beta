/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.avc.mis.beta.dto.data.UserLogin;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.utilities.UserAware;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Abstract root base class for all data access objects.
 * 
 * @author Zvi
 *
 */
@Getter(value = AccessLevel.PACKAGE)
public abstract class ReadDAO {
	
	@Autowired private EntityManager entityManager;
	@Autowired UserAware userAware;

	/**
	 * Gets the logged in user id.
	 * @return the id of currently logged in user.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public Integer getCurrentUserId() {
		Optional<UserLogin> userEntity = getUserAware().getCurrentUser();
		userEntity.orElseThrow(() -> new IllegalStateException("No user logged in or user not reachable"));
		return userEntity.get().getId();
	}
	
	UserLogin getCurrentUser() {
		return getUserAware().getCurrentUser().orElseThrow(() -> new IllegalStateException("No user logged in or user not reachable"));
	}

	public <T extends BaseEntity> boolean isTableEmpty(Class<T> entityClass) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(entityClass)));
		return entityManager.createQuery(cq).getSingleResult() == 0L;
		
	
	}

}
