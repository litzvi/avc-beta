/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.SoftDeletableDAO;
import com.avc.mis.beta.dto.data.BankAccountDTO;
import com.avc.mis.beta.dto.data.CompanyContactDTO;
import com.avc.mis.beta.dto.data.PaymentAccountDTO;
import com.avc.mis.beta.dto.data.PersonDTO;
import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.values.SupplierRow;
import com.avc.mis.beta.dto.values.ValueObject;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.PaymentAccount;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.repositories.SupplierRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 
 * A service for manipulating entities related to Business Contacts.
 * Currently only for supplier, but should be adjusted for all Business Contacts -
 * perhaps names should be changed as well.
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(rollbackFor = Throwable.class)
public class Suppliers {

	@Autowired private SoftDeletableDAO dao;
	
	@Autowired private DeletableDAO deletableDAO;
	
	@Autowired private SupplierRepository supplierRepository;
	
	/**
	 * Get Table of all suppliers with partial info - id, name, emails, phones and supply categories -
	 * to show in the table.
	 * @return List of SupplierRow of all suppliers
	 */
	@Transactional(readOnly = true)
	public List<SupplierRow> getSuppliersTable() {
		
		List<SupplierRow> supplierRows = getSupplierRepository().findAllSupplierRows();
		Map<Integer, Set<String>> phones = getSupplierRepository().findAllPhoneValues()
				.collect(Collectors.groupingBy(ValueObject<String>::getId, 
						Collectors.mapping(ValueObject<String>::getValue, Collectors.toSet())));
		Map<Integer, Set<String>> emails = getSupplierRepository().findAllEmailValues()
				.collect(Collectors.groupingBy(ValueObject<String>::getId, 
						Collectors.mapping(ValueObject<String>::getValue, Collectors.toSet())));
		Map<Integer, Set<String>> categories = getSupplierRepository().findAllSupplyCategoryValues()
				.collect(Collectors.groupingBy(ValueObject<String>::getId, 
						Collectors.mapping(ValueObject<String>::getValue, Collectors.toSet())));
		supplierRows.forEach((s) -> {
			s.setPhones(phones.get(s.getContactDetailsId())); 
			s.setEmails(emails.get(s.getContactDetailsId()));
			s.setSupplyCategories(categories.get(s.getId()));
		});
		return supplierRows;		
	}	
		
	/**
	 * Adds (persists) the given supplier with all information - 
	 * assumes all references and data wher'nt previously inserted.
	 * @param supplier the added Supplier
	 * @throws IllegalArgumentException if supplier name isn't set or not legal.
	 */
	public void addSupplier(Supplier supplier) {
		dao.addEntity(supplier);
		for(CompanyContact contact: supplier.getCompanyContacts()) {
			Person person = contact.getPerson();
			if(person.getId() == null) {
				dao.addEntity(person);
			}
			dao.addEntity(contact);			
		}		
	}
	
	/**
	 * Gets all supplier information for given supplier id
	 * @param supplierId of the supplier to get.
	 * @return SuppplierDTO with full supplier information
	 * @throws IllegalArgumentException if supplier with given id doesn't exist
	 */
	@Transactional(readOnly = true)
	public SupplierDTO getSupplier(int supplierId) {
		
		Optional<Supplier> optionalSupplier = getSupplierRepository().findById(supplierId);
		Supplier supplier = optionalSupplier.orElseThrow(() -> 
			new IllegalArgumentException("No supplier with given ID"));
		SupplierDTO supplierDTO = new SupplierDTO(supplier, false);
		System.out.println("line 116");
		getSupplierRepository().findCompanyContactsByCompnyId(supplierId)
			.forEach((cc) -> supplierDTO.addCompanyContact(cc));
		
		return supplierDTO;
	}
	
	/**
	 * Soft deletes company.
	 * Dosen't remove stand alone entities created with the company.
	 * Specifically, dosen't remove persons and bank accounts.
	 * @param supplierId
	 */
	public void removeSupplier(int supplierId) {
//		SoftDeleted entity = getEntityManager().getReference(Supplier.class, supplierId);
//		removeEntity(entity);	
		dao.removeEntity(Supplier.class, supplierId);
	}
	
	/**
	 * For testing only
	 * @param supplierId
	 */
	@Deprecated
	public void permenentlyRemoveSupplier(int supplierId) {
		SupplierDTO supplier = getSupplier(supplierId);
		for(PaymentAccountDTO paymentAccount: supplier.getContactDetails().getPaymentAccounts())  {
			BankAccountDTO bankAccount = paymentAccount.getBankAccount();
			getDeletableDAO().permenentlyRemoveEntity(BankAccount.class, bankAccount.getId());
		}
		for(CompanyContactDTO companyContact: supplier.getCompanyContacts())  {
			PersonDTO person = companyContact.getPerson();
			getDeletableDAO().permenentlyRemoveEntity(CompanyContact.class, companyContact.getId());
			getDeletableDAO().permenentlyRemoveEntity(Person.class, person.getId());
		}
		getDeletableDAO().permenentlyRemoveEntity(Supplier.class, supplier.getId());
	}
	
	/**
	 * Edits supplier main (company) Information - 
	 * local name, english name, license, tax code, registration location and supply categories
	 * @param supplier with all Supplier editable main information set to state after edit.
	 * @return the edited supplier
	 */
	public Supplier editSupplierMainInfo(Supplier supplier) {
//		dao.editEntity(supplier);
		return (Supplier)dao.editEntity(supplier);
	}
	
	/**
	 * Edits contact details information -
	 * phones, emails, faxes address and payment accounts.
	 * @param contactDetails with all ContactDetails editable details set to state after edit.
	 */
	public void editContactInfo(ContactDetails contactDetails) {
		dao.editEntity(contactDetails);
	}
	
	/**
	 * Edits PaymentAccount information - ordinal and bank account
	 * @param account with all PaymentAccount editable details set to state after edit.
	 */
	public void editAccount(PaymentAccount account) {
		dao.editEntity(account);
	}
	
	/**
	 * Edits CompanyContact information - Person, person editable details and CompanyPosition
	 * @param contact with all CompanyContact and Person editable details set to state after edit.
	 * @throws IllegalArgumentException if the given CompanyContact dosen't reference a person 
	 * or person has no qualified name.
	 */
	public void editContactPerson(CompanyContact contact) {
		dao.editEntity(contact);
		Person person = contact.getPerson();
		if(person != null && person.isLegal()) {
			dao.editEntity(person);
			dao.editEntity(person.getContactDetails());
		}
		else {
			throw new IllegalArgumentException("Company contact not edited, missing or illegal information");
		}
	}
		
	/**
	 * Adds a new payment account to existing Company or Person
	 * @param account with account information
	 * @param contactId ContactDetails id of the account owner
	 */
	public void addAccount(PaymentAccount account, int contactId) {
//		ContactDetails contactDetails = getEntityManager().getReference(ContactDetails.class, contactId);
//		account.setContactDetails(contactDetails);
		dao.addEntity(account, ContactDetails.class, contactId);
	}
	
	/**
	 * Removes account from ContactDetails
	 * @param accountId
	 */
	public void removeAccount(int accountId) {
//		BaseEntity entity = getEntityManager().getReference(PaymentAccount.class, accountId);
		getDeletableDAO().permenentlyRemoveEntity(PaymentAccount.class, accountId);	
	}
	
	/**
	 * Adds a new company contact to a company, assumes the is transient - dosen't exist in the database.
	 * @param contact with CopmanyPosition and Person information.
	 * @param companyId id of Company the contact belongs to.
	 * @throws IllegalArgumentException if person isn't set or has a non qualifying name.
	 */
	public void addContactPerson(CompanyContact contact, int companyId) {
//		Company company = getEntityManager().getReference(Company.class, companyId);
//		contact.setCompany(company);
		Person person = contact.getPerson();
		if(person != null) {
			if(person.getId() != null) {
				//need to decide if to edit person
				dao.setEntityReference(contact, Person.class, person.getId());
				dao.addEntity(contact, Company.class, companyId);
			}
			else if(person.isLegal()) {
				dao.addEntity(person);
//				getEntityManager().persist(contact);
				dao.addEntity(contact, Company.class, companyId);
			}
			else {
				throw new IllegalArgumentException("Person information is illegal");
			}
		}
		else {
			throw new IllegalArgumentException("Company contact has to reference an existing or new person");
		}
	}
	
	/**
	 * Soft remove the CompanyContact - flags the contact as not active
	 * @param contactId id of CompanyContact to remove
	 */
	public void removeContactPerson(int contactId) {
//		SoftDeleted entity = getEntityManager().getReference(CompanyContact.class, contactId);
		dao.removeEntity(CompanyContact.class, contactId);
	}
	
	/**
	 * For testing only, needed because calling DAO directly has no transaction
	 * @param id
	 */
	@Deprecated
	public void addEntity(BaseEntity entity, BaseEntity reference) {
		dao.addEntity(entity, reference);
	}
	
	/**
	 * For testing only, needed because calling DAO directly has no transaction
	 * @param identity
	 */
	@Deprecated
	public void permenentlyRemoveEntity(BaseEntity entity) {
		getDeletableDAO().permenentlyRemoveEntity(entity);
	}

	/**
	 * For testing only, needed because calling DAO directly has no transaction
	 * @param entity
	 */
	@Deprecated
	public void editEntity(BaseEntity entity) {
		dao.editEntity(entity);
	}
	
}
