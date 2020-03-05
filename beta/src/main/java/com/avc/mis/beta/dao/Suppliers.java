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

import com.avc.mis.beta.dataobjects.CompanyContact;
import com.avc.mis.beta.dataobjects.CompanyContactPK;
import com.avc.mis.beta.dataobjects.ContactDetails;
import com.avc.mis.beta.dataobjects.PaymentAccount;
import com.avc.mis.beta.dataobjects.Person;
import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dto.CompanyContactDTO;
import com.avc.mis.beta.dto.SupplierDTO;
import com.avc.mis.beta.dto.SupplierRow;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class Suppliers extends DAO {
	
//	@Autowired
//	private SupplierReposetory supplierReposetory;
	
		
	public List<SupplierRow> getSuppliers() {
		
		TypedQuery<Supplier> query = getEntityManager().createNamedQuery("Supplier.findAll", Supplier.class);
		List<Supplier> suppliers = query.getResultList();
		List<SupplierRow> supplierRows = new ArrayList<>();
		suppliers.forEach((supplier) -> supplierRows.add(new SupplierRow(supplier)));
		return supplierRows;
		
	}
		
	/**
	 * 
	 * @param supplier
	 * @return
	 */
	public void addSupplier(Supplier supplier) {
		if(supplier == null || !supplier.isLegal()) {
			throw new IllegalArgumentException("Supplier missing required details");
		}
		getEntityManager().persist(supplier);
		for(CompanyContact contact: supplier.getCompanyContacts()) {
			Person person = contact.getPerson();
			if(person != null && person.isLegal()) {
				getEntityManager().persist(person);
				getEntityManager().persist(contact);
			}
			
		}
//		getEntityManager().flush();
	}
	
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
	
	/*
	 * public List getSuppliersBasic() {
	 * 
	 * Query query = getEntityManager().createNativeQuery( "Supplier.findAllBasic");
	 * return query.getResultList();
	 * 
	 * }
	 */
	
//	public void editSupplierInformation(Supplier supplier) {
////		getEntityManager().find(Supplier.class, supplier.getId()).setName("findName");
//		getEntityManager().merge(supplier);
//		getEntityManager().flush();
//	}
	
	
	
//	public void editSupplier() {
//		
//	}
	
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
	
	public void editSupplierMainInfo(Supplier supplier) {
		if(supplier == null || !supplier.isLegal()) {
			throw new IllegalArgumentException("Edited supplier missing required details");
		}
		getEntityManager().merge(supplier);
	}
	
	public void editContactInfo(ContactDetails contactDetails) {
		getEntityManager().merge(contactDetails);
	}
	
	public void editAccount(PaymentAccount account) {
		if(account == null || !account.isLegal()) {
			throw new IllegalArgumentException("Edited account missing required details");
		}
		getEntityManager().merge(account);
	}
	
	public void addAccount(PaymentAccount account, int contactId) {
		if(account == null || !account.isLegal()) {
			throw new IllegalArgumentException("Account missing required details");
		}
		ContactDetails contactDetails = getEntityManager().getReference(ContactDetails.class, contactId);
		account.setContactDetails(contactDetails);
		getEntityManager().persist(account);
	}
	
	public void removeAccount(int accountId) {
		PaymentAccount account = getEntityManager().getReference(PaymentAccount.class, accountId);
		getEntityManager().remove(account);
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
	
	public void editContactPerson(CompanyContact contact) {
		Person person = contact.getPerson();
		if(person != null && person.isLegal()) {
			getEntityManager().merge(person);
			getEntityManager().merge(person.getContactDetails());
		}
		else {
			throw new IllegalArgumentException("Company contact not edited, missing or illegal information");
		}
	}

	public void removeContactPerson(CompanyContact contact) {
		contact = getEntityManager()
				.getReference(CompanyContact.class, new CompanyContactPK(contact.getPerson(), contact.getCompany()));
		getEntityManager().remove(contact);
	}	
	
}
