/**
 * 
 */
package com.avc.mis.beta.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.data.SupplierRow;
import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.PaymentAccount;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.repositories.SupplierRepository;

/**
 * @author Zvi
 *
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class Suppliers extends DAO {
	
	@Autowired
	private SupplierRepository supplierRepository;
		
	/**
	 * @return the supplierRepository
	 */
	private SupplierRepository getSupplierRepository() {
		return supplierRepository;
	}

	@Transactional(readOnly = true)
	public List<SupplierBasic> getSuppliersBasic() {
		return getSupplierRepository().findAllSuppliersBasic();
	}
		
	@Transactional(readOnly = true)
	public List<SupplierRow> getSuppliersTable() {
		List<SupplierRow> supplierRows = new ArrayList<>();
		getSupplierRepository().findAll().forEach((s) -> supplierRows.add(new SupplierRow(s)));
		return supplierRows;		
	}
	
	@Transactional(readOnly = true)
	public List<SupplierBasic> getSuppliersBasic(Integer categoryId) {
		return getSupplierRepository().findSuppliersByCategoryBasic(categoryId);
	}
		
	/**
	 * 
	 * @param supplier
	 * @return
	 */
	public void addSupplier(Supplier supplier) {
		getEntityManager().persist(supplier);
		for(CompanyContact contact: supplier.getCompanyContacts()) {
			Person person = contact.getPerson();
			getEntityManager().persist(person);
			getEntityManager().persist(contact);			
		}
	}
	
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
		removeEntity(Supplier.class, supplierId);	
	}
	
	/**
	 * For testing only
	 * @param supplierId
	 */
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
	
	public void editSupplierMainInfo(Supplier supplier) {
		editEntity(supplier);
	}
	
	public void editContactInfo(ContactDetails contactDetails) {
		editEntity(contactDetails);
	}
	
	public void editAccount(PaymentAccount account) {
		editEntity(account);
	}	
	
	public void addAccount(PaymentAccount account, int contactId) {
		ContactDetails contactDetails = getEntityManager().getReference(ContactDetails.class, contactId);
		account.setContactDetails(contactDetails);
		getEntityManager().persist(account);
	}
	
	public void removeAccount(int accountId) {
		removeEntity(PaymentAccount.class, accountId);	
	}
	
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
	
	public void addContactPerson(CompanyContact contact, int supplierId) {
		Supplier supplier = getEntityManager().getReference(Supplier.class, supplierId);
		contact.setCompany(supplier);
		Person person = contact.getPerson();
		if(person != null && person.isLegal()) {
			getEntityManager().persist(person);
			getEntityManager().persist(contact);
		}
		else {
			throw new IllegalArgumentException("Missing or illegal information for new company contact");
		}
	}
	
	public void removeContactPerson(int contactId) {
		removeEntity(CompanyContact.class, contactId);
	}
	
	//for testing - should be removed
	public void addEntity(Insertable entity, Insertable reference) {
		super.addEntity(entity, reference);
	}
	
	//for testing - should be removed
	public void removeEntity(Insertable entity) {
		super.removeEntity(entity);
	}
	
	//for testing - should be removed
	public void editEntity(Insertable entity) {
		super.editEntity(entity);
	}	
}
