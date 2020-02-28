/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.dataobjects.Supplier;

/**
 * @author Zvi
 *
 */
public interface SupplierReposetory extends JpaRepository<Supplier, Integer> {

//	@EntityGraph(attributePaths = {
//			"supplyCategories", 
//			"contactDetails",  
//				"contactDetails.phones", "contactDetails.faxes", "contactDetails.emails", 
//				"contactDetails.addresses", 
//					"contactDetails.addresses.city", 
//				"contactDetails.paymentAccounts", 
//					"contactDetails.paymentAccounts.bankAccount",
//						"contactDetails.paymentAccounts.bankAccount.branch",
//							"contactDetails.paymentAccounts.bankAccount.branch.bank",
//			"companyContacts",
//				"companyContacts.person", 
//					"companyContacts.person.idCard",
//						"companyContacts.person.idCard.nationality",
//					"companyContacts.person.contactDetails",
//						"companyContacts.person.contactDetails.phones", 
//						"companyContacts.person.contactDetails.faxes", 
//						"companyContacts.person.contactDetails.emails",
//						"companyContacts.person.contactDetails.addresses", 
//							"companyContacts.person.contactDetails.addresses.city", 
//				"companyContacts.position"})
//	@Query(value = "select s from supplier s where s.id = ?1")
//	Optional<Supplier> findSupplierById(Integer id);
	
}
