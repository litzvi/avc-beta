package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.dao.ReferenceTables;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dataobjects.BankAccount;
import com.avc.mis.beta.dataobjects.BankBranch;
import com.avc.mis.beta.dataobjects.CompanyContact;
import com.avc.mis.beta.dataobjects.ContactDetails;
import com.avc.mis.beta.dataobjects.Email;
import com.avc.mis.beta.dataobjects.Fax;
import com.avc.mis.beta.dataobjects.IdCard;
import com.avc.mis.beta.dataobjects.PaymentAccount;
import com.avc.mis.beta.dataobjects.Person;
import com.avc.mis.beta.dataobjects.Phone;
import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dataobjects.SupplyCategory;
import com.avc.mis.beta.dto.SupplierDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

@SpringBootTest
class BetaApplicationTests {
	
	@Autowired
	Suppliers suppliers;
	
	@Autowired
	ReferenceTables referenceTables;
	
	private static Integer SERIAL_NO = 1085;

	@Disabled
	@Test
	void getSupplierSuccesfulTest() throws JsonProcessingException {
//		ObjectMapper onjMapper = new ObjectMapper(); 
//		String supplierJson = onjMapper.writeValueAsString(suppliers.getSupplier(196)); 
//		System.out.println(supplierJson);
		
		System.out.println(suppliers.getSupplier(196));
	}
	
	@Disabled
	@Test
	void getSuppliersTableTest() {
		suppliers.getSuppliers().forEach(supplier -> System.out.println(supplier));
	}
	
	private Supplier buildSupplier(String name) {
		Supplier supplier = new Supplier();
		supplier.setName(name);
		
		List<SupplyCategory> supplyCategories = referenceTables.getAllSupplyCategories();
		supplyCategories.forEach(category -> supplier.getSupplyCategories().add(category));
		
		ContactDetails contactDetails = new ContactDetails();
		supplier.setContactDetails(contactDetails);
		for(int i=0; i<2; i++) {
			Phone phone = new Phone();
			Fax fax = new Fax();
			Email email = new Email();
			phone.setName("phone" + i);
			fax.setName("fax" + i);
			email.setName("email" + i);
			contactDetails.getPhones().add(phone);
			contactDetails.getFaxes().add(fax);
			contactDetails.getEmails().add(email);
			
		}
		PaymentAccount paymentAccount = new PaymentAccount();		
		BankAccount bankAccount = new BankAccount();
		bankAccount.setOwnerName("ownerName" + name);
		bankAccount.setAccountNo("accountNo" + name);
		BankBranch bankBranch = new BankBranch();
		bankBranch.setId(1);
		bankAccount.setBranch(bankBranch);
		paymentAccount.setBankAccount(bankAccount);
		contactDetails.getPaymentAccounts().add(paymentAccount);		

		CompanyContact companyContact;
		Person person;
		for(int i=0; i<2; i++) {
			companyContact = new CompanyContact();
			person = new Person();
			person.setName("person" + i);
			IdCard idCard = new IdCard();
			idCard.setIdNumber("id card" + i);
			person.setIdCard(idCard);
			companyContact.setPerson(person);
			supplier.getCompanyContacts().add(companyContact);
		}
		
		
		return supplier;
		
	}
	
	@Test
	void editSupplierIsSuccessfulTest() {
		Supplier supplier = buildSupplier("remove test" + SERIAL_NO);
		supplier.getSupplyCategories().addAll(referenceTables.getAllSupplyCategories());
		suppliers.addSupplier(supplier);
		Integer id = supplier.getId();
		SupplierDTO addedSupplier = suppliers.getSupplier(id);
		supplier.setEnglishName("englishName" + SERIAL_NO);
		supplier.setName("edit name test" + SERIAL_NO);
		suppliers.editSupplierMainInfo(supplier);
		SupplierDTO editedSupplier = suppliers.getSupplier(id);
		ContactDetails contactDetails = supplier.getContactDetails();
		Phone phoneToRemove = contactDetails.getPhones().iterator().next();
		contactDetails.getPhones().remove(phoneToRemove);
		supplier.getSupplyCategories().removeAll(referenceTables.getAllSupplyCategories());
		System.out.println(contactDetails);
		suppliers.editContactInfo(contactDetails);
		System.out.println(addedSupplier);
		System.out.println(editedSupplier);
		System.out.println(suppliers.getSupplier(id));
//		suppliers.removeSupplier(id);
//		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> suppliers.getSupplier(id));
		
		
	}
	
	@Test
	void insertSupplierIsSuccessfulTest() {	
		
		//supplier with name only, creates a record for contact details for company
		//supplier with duplicate name
		//not adding an empty phone/email/fax/address
		//address without a city
		//adding non existing city
		//adding 0/1/multiple categories
		//default supplier is active
		//adding 0/1/multiple company contacts
		//adding 0/1/multiple phones/emails/faxes/addresses
		//rolling back failed insert
		//non existing city to address
		//inserting 2 accounts with the same info
		//never crash the program
		//check if can add existing bank account
		//check if can add existing person to company contact
		//check inserting 0/1/many company contacts
		//check if all fileds are fetched when using jakson toJson
				
		
//		HashSet<SupplyCategory> supplyCategories = new HashSet<>();
//		supplyCategories.addAll(referenceTables.getAllSupplyCategories());
//		List<Object[]> suppliersList = suppliers.getSuppliersBasic();
//		for(Object[] objectArray: suppliersList) {
//			System.out.println(objectArray);
//		}
		//EDIT
//		supplyCategories = new HashSet<>();
//		supplier.setSupplyCategories(supplyCategories);
//		supplier.setLicense("license " + SERIAL_NO);
////		System.out.println(supplier.getContactDetails());
////		supplier.setContactDetails(new ContactDetails());
//		suppliers.editSupplierInformation(supplier);
		
	}
	

	@Test
	void insertSupplyCategoriesTest() {
		
//		SupplyCategory category = new SupplyCategory();
//		category.setName("Bags " + SERIAL_NO);
//		referenceTables.insertSupplyCategory(category);
		
	}

}
