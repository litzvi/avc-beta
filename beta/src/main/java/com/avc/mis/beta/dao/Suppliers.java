/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.SupplierBasic;
import com.avc.mis.beta.dto.SupplierDTO;
import com.avc.mis.beta.dto.SupplierRow;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.CompanyContactPK;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.PaymentAccount;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.interfaces.Insertable;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class Suppliers extends DAO {
	
//	@Autowired
//	private SupplierReposetory supplierReposetory;
	
	private List<Supplier> findSuppliers() {
		TypedQuery<Supplier> query = getEntityManager().createNamedQuery("Supplier.findAll", Supplier.class);
		List<Supplier> suppliers = query.getResultList();
		return suppliers;
	}
	
	@Transactional(readOnly = true)
	public List<SupplierBasic> getSuppliersBasic() {
		List<SupplierBasic> supplierRows = new ArrayList<>();
		findSuppliers().forEach((supplier) -> supplierRows.add(new SupplierBasic(supplier)));
		return supplierRows;
		
	}
		
	@Transactional(readOnly = true)
	public List<SupplierRow> getSuppliersTable() {
		
		List<SupplierRow> supplierRows = new ArrayList<>();
		findSuppliers().forEach((supplier) -> supplierRows.add(new SupplierRow(supplier)));
		return supplierRows;
		
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
		
		TypedQuery<Supplier> querySupplyer = getEntityManager().createNamedQuery("Supplier.details", Supplier.class);
		querySupplyer.setParameter("sid", id);
		Supplier supplier;
		try {
			supplier = querySupplyer.getSingleResult();
		}
		catch(NoResultException e) {
			throw new IllegalArgumentException("No supplier with given ID");
		}
		SupplierDTO supplierDTO = new SupplierDTO(supplier);
		
		TypedQuery<CompanyContact> queryContacts = 
				getEntityManager().createNamedQuery("CompanyContact.details.findAll", CompanyContact.class);
		queryContacts.setParameter("cid", id);
		List<CompanyContact> supplierContacts = queryContacts.getResultList();
		supplierContacts.forEach((contact) -> supplierDTO.addCompanyContact(contact));
		
		return supplierDTO;
	}
	
	/**
	 * Soft deletes company.
	 * Dosen't remove stand alone entities created with the company.
	 * Specifically, dosen't remove persons and bank accounts.
	 * @param supplierId
	 */
	public void removeSupplier(int supplierId) {
		Supplier supplier = getEntityManager().getReference(Supplier.class, supplierId);
		getEntityManager().remove(supplier);
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
		PaymentAccount account = getEntityManager().getReference(PaymentAccount.class, accountId);
		getEntityManager().remove(account);
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
	
	public void removeContactPerson(CompanyContact contact) {
		contact = getEntityManager()
				.getReference(CompanyContact.class, new CompanyContactPK(contact.getPerson(), contact.getCompany()));
		getEntityManager().remove(contact);
	}
	
	public void addEntity(Insertable entity, Insertable reference) {
		reference = getEntityManager().getReference(reference.getClass(), reference.getId());
		entity.setReference(reference);
		getEntityManager().persist(entity);
	}
	
	public void removeEntity(Insertable entity) {
		entity = getEntityManager().getReference(entity.getClass(), entity.getId());
		getEntityManager().remove(entity); 
	}
	
	public void editEntity(Insertable entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		getEntityManager().merge(entity);
	}	
}
