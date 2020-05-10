/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessAlert;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.process.PoCode;

/**
 * @author Zvi
 *
 */
public interface ObjectTablesRepository extends BaseRepository<ObjectEntityWithId> {
	
//-----------Finding all Base entities--------------
	
	@Query("select e from PoCode e")
	List<PoCode> findAllPoCodes();
	
//-----------Finding all Object entities with active=true--------------

	@Query("select e from BankAccount e where e.active = true")
	List<BankAccount> findAllBankAccounts();

	@Query("select e from Company e where e.active = true")
	List<Company> findAllCompanies();

	@Query("select e from Person e where e.active = true")
	List<Person> findAllPersons();

	@Query("select e from CompanyContact e where e.active = true")
	List<CompanyContact> findAllCompanyContacts();
	
	@Query("select e from UserEntity e where e.active = true")
	List<UserEntity> findAllUsers();
	
//------------Finding all Link entities------------

	@Query("select e from ProcessAlert e")
	List<ProcessAlert> findAllProcessAlerts();

	@Query("select e from ContactDetails e")
	List<ContactDetails> findAllContactDetails();

	
}