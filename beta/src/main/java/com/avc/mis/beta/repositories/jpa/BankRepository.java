/**
 * 
 */
package com.avc.mis.beta.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.entities.values.Bank;

/**
 * @author Zvi
 *
 */
public interface BankRepository extends JpaRepository<Bank, Integer> {
	
//	@Query(value = "update banks set id = :newId where id = :oldId", nativeQuery = true)
//	void setBankId(int newId, int oldId);
}
