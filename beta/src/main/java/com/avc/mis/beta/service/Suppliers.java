/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Optional;

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
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.PaymentAccount;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.repositories.SupplierRepository;
import com.avc.mis.beta.service.report.SupplierReports;

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
	 * Adds (persists) the given supplier with all information - 
	 * assumes all references and data whern't previously inserted.
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
		dao.removeEntity(Supplier.class, supplierId);
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
	public void editContactInfo(ContactDetails contactDetails, int companyId) {
		if(contactDetails.getId() == null) {
			dao.addEntity(contactDetails, Company.class, companyId);
		}
		else {
			dao.editEntity(contactDetails);
		}
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
		if(person != null && person.getContactDetails() != null) {
			dao.editEntity(person);
			dao.editEntity(person.getContactDetails());
		}
		else { //perhaps can remove, in case of editing without changing person details
			throw new IllegalArgumentException("Company contact not edited, not referencing person");
		}
	}
		
	/**
	 * Adds a new payment account to existing Company or Person
	 * @param account with account information
	 * @param contactId ContactDetails id of the account owner
	 */
	public void addAccount(PaymentAccount account, int contactId) {
		if(account.getOrdinal() == null) {
			account.setOrdinal(0);
		}
		dao.addEntity(account, ContactDetails.class, contactId);
	}
	
	/**
	 * Removes account from ContactDetails
	 * @param accountId
	 */
	public void removeAccount(int accountId) {
		getDeletableDAO().permenentlyRemoveEntity(PaymentAccount.class, accountId);	
	}
	
	/**
	 * Adds a new company contact to a company, assumes the is transient - dosen't exist in the database.
	 * @param contact with CopmanyPosition and Person information.
	 * @param companyId id of Company the contact belongs to.
	 * @throws IllegalArgumentException if person isn't set or has a non qualifying name.
	 */
	public void addContactPerson(CompanyContact contact, int companyId) {
		Person person = contact.getPerson();
		if(person == null) {
			throw new IllegalArgumentException("Company contact has to reference an existing or new person");
		}
		
		if(person.getId() != null) {
			//need to decide if to edit person
			dao.setEntityReference(contact, Person.class, person.getId());
		}
		else {
			dao.addEntity(person);
		}
		dao.addEntity(contact, Company.class, companyId);
	}

	//----------------------------Duplicate in SupplierReports - Should remove------------------------------------------

	@Autowired private SupplierReports supplierReports;
	
	/**
	 * Get Table of all suppliers with partial info - id, name, emails, phones and supply categories -
	 * to show in the table.
	 * @return List of SupplierRow of all suppliers
	 */
	@Transactional(readOnly = true)
	public List<SupplierRow> getSuppliersTable() {
		return getSupplierReports().getSuppliersTable();
	}	

	//----------------------------For testing only------------------------------------------
	
	/**
	 * For testing only
	 * @param supplierId
	 */
	@Deprecated
	public void permenentlyRemoveSupplier(int supplierId) {
		SupplierDTO supplier = getSupplier(supplierId);
		if(supplier.getContactDetails() != null) {
			for(PaymentAccountDTO paymentAccount: supplier.getContactDetails().getPaymentAccounts())  {
				BankAccountDTO bankAccount = paymentAccount.getBankAccount();
				getDeletableDAO().permenentlyRemoveEntity(BankAccount.class, bankAccount.getId());
			}
		}
		for(CompanyContactDTO companyContact: supplier.getCompanyContacts())  {
			PersonDTO person = companyContact.getPerson();
			getDeletableDAO().permenentlyRemoveEntity(CompanyContact.class, companyContact.getId());
			getDeletableDAO().permenentlyRemoveEntity(Person.class, person.getId());
		}
		getDeletableDAO().permenentlyRemoveEntity(Supplier.class, supplier.getId());
	}

	
}
