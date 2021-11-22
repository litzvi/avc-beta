/**
 * 
 */
package com.avc.mis.beta.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avc.mis.beta.entities.values.Bank;

/**
 * @author Zvi
 *
 */
public interface BankRepository extends JpaRepository<Bank, Integer> {
}
