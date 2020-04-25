/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.dto.values.SupplierRow;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.SoftDeleted;
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
 * A service for manipulating entities related to Business Contacts.
 * Currently only for supplier, but should be adjusted for all Business Contacts -
 * perhaps names should be changed as well.
 * 
 * @author Zvi
 *
 */
@Repository
@Getter(value = AccessLevel.PRIVATE)
@Transactional(rollbackFor = Throwable.class)
public class Suppliers extends SoftDeletableDAO {
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	/**
	 * Get a list of all suppliers basic information -  id, name and version.
	 * Usually used for referencing a supplier in another process.
	 * @return List of SupplierBasic of all existing suppliers.
	 */
	@Transactional(readOnly = true)
	public List<SupplierBasic> getSuppliersBasic() {
		return getSupplierRepository().findAllSuppliersBasic();
	}
		
	/**
	 * Get Table of all suppliers with partial info - id, name, emails, phones and supply categories -
	 * to show in the table.
	 * @return List of SupplierRow of all suppliers
	 */
	@Transactional(readOnly = true)
	public List<SupplierRow> getSuppliersTable() {
		List<SupplierRow> supplierRows = getSupplierRepository().findAll()
				.map((s) -> new SupplierRow(s)).collect(Collectors.toList());
		return supplierRows;		
	}
	
	/**
	 * Get a list of suppliers basic information -  id, name and version - for given supply category.
	 * @param categoryId id of SupplyCategory
	 * @return List of SupplierRow of all suppliers with given SupplyCategory
	 */
	@Transactional(readOnly = true)
	public List<SupplierBasic> getSuppliersBasic(Integer categoryId) {
		return getSupplierRepository().findSuppliersByCategoryBasic(categoryId);
	}
		
	/**
	 * Adds (persists) the given supplier with all information - 
	 * assumes all references and data wher'nt previously inserted.
	 * @param supplier the added Supplier
	 * @throws IllegalArgumentException if supplier name isn't set or not legal.
	 */
	public void addSupplier(Supplier supplier) {
		getEntityManager().persist(supplier);
		for(CompanyContact contact: supplier.getCompanyContacts()) {
			Person person = contact.getPerson();
			getEntityManager().persist(person);
			getEntityManager().persist(contact);			
		}		
	}
	
	/**
	 * Gets all supplier information for given supplier id
	 * @param id of the supplier to get.
	 * @return SuppplierDTO with full supplier information
	 * @throws IllegalArgumentException if supplier with given id doesn't exist
	 */
	@Transactional(readOnly = true)
	public SupplierDTO getSupplier(int id) {
		
		Optional<Supplier> optionalSupplier = getSupplierRepository().findById(id);
		Supplier supplier = optionalSupplier.orElseThrow(() -> 
			new IllegalArgumentException("No supplier with given ID"));
		SupplierDTO supplierDTO = new SupplierDTO(supplier);
		
		getSupplierRepository().findCompanyContactsByCompnyId(id)
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
		SoftDeleted entity = getEntityManager().getReference(Supplier.class, supplierId);
		removeEntity(entity);	
	}
	
	/**
	 * For testing only
	 * @param supplierId
	 */
	@Deprecated
	public void permenentlyRemoveSupplier(int supplierId) {
		Supplier supplier = getEntityManager().getReference(Supplier.class, supplierId);
		for(PaymentAccount paymentAccount: supplier.getContactDetails().getPaymentAccounts())  {
			BankAccount bankAccount = paymentAccount.getBankAccount();
			getEntityManager().remove(bankAccount);
		}
		for(CompanyContact companyContact: supplier.getCompanyContacts())  {
			Person person = companyContact.getPerson();
			getEntityManager().remove(companyContact);
			getEntityManager().remove(person);
		}
		getEntityManager().remove(supplier);
	}
	
	/**
	 * Edits supplier main (company) Information - 
	 * local name, english name, license, tax code, registration location and supply categories
	 * @param supplier with all Supplier editable main information set to state after edit.
	 * @return the edited supplier
	 */
	public Supplier editSupplierMainInfo(Supplier supplier) {
		return (Supplier)editEntity(supplier);
	}
	
	/**
	 * Edits contact details information -
	 * phones, emails, faxes address and payment accounts.
	 * @param contactDetails with all ContactDetails editable details set to state after edit.
	 */
	public void editContactInfo(ContactDetails contactDetails) {
		editEntity(contactDetails);
	}
	
	/**
	 * Edits PaymentAccount information - ordinal and bank account
	 * @param account with all PaymentAccount editable details set to state after edit.
	 */
	public void editAccount(PaymentAccount account) {
		editEntity(account);
	}
	
	/**
	 * Edits CompanyContact information - Person, person editable details and CompanyPosition
	 * @param contact with all CompanyContact and Person editable details set to state after edit.
	 * @throws IllegalArgumentException if the given CompanyContact dosen't reference a person 
	 * or person has no qualified name.
	 */
	public void editContactPerson(CompanyContact contact) {
		editEntity(contact);
		Person person = contact.getPerson();
		if(person != null && person.isLegal()) {
			editEntity(person);
			editEntity(person.getContactDetails());
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
		ContactDetails contactDetails = getEntityManager().getReference(ContactDetails.class, contactId);
		account.setContactDetails(contactDetails);
		addEntity(account);
	}
	
	
	/**
	 * Removes account from ContactDetails
	 * @param accountId
	 */
	public void removeAccount(int accountId) {
		BaseEntity entity = getEntityManager().getReference(PaymentAccount.class, accountId);
		permenentlyRemoveEntity(entity);	
	}
	
	/**
	 * Adds a new company contact to a company, assumes the is transient - dosen't exist in the database.
	 * @param contact with CopmanyPosition and Person information.
	 * @param companyId id of Company the contact belongs to.
	 * @throws IllegalArgumentException if person isn't set or has a non qualifying name.
	 */
	public void addContactPerson(CompanyContact contact, int companyId) {
		Company company = getEntityManager().getReference(Company.class, companyId);
		contact.setCompany(company);
		Person person = contact.getPerson();
		if(person != null && person.isLegal()) {
			getEntityManager().persist(person);
			getEntityManager().persist(contact);
		}
		else {
			throw new IllegalArgumentException("Missing or illegal information for new company contact");
		}
	}
	
	/**
	 * Soft remove the CompanyContact - flags the contact as not active
	 * @param contactId id of CompanyContact to remove
	 */
	public void removeContactPerson(int contactId) {
		SoftDeleted entity = getEntityManager().getReference(CompanyContact.class, contactId);
		removeEntity(entity);
	}
	
}
