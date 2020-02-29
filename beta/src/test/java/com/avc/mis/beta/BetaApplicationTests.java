package com.avc.mis.beta;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.dao.ReferenceTables;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dataobjects.BankAccount;
import com.avc.mis.beta.dataobjects.BankBranch;
import com.avc.mis.beta.dataobjects.CompanyContact;
import com.avc.mis.beta.dataobjects.ContactDetails;
import com.avc.mis.beta.dataobjects.PaymentAccount;
import com.avc.mis.beta.dataobjects.Person;
import com.avc.mis.beta.dataobjects.Phone;
import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dataobjects.SupplyCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

@SpringBootTest
class BetaApplicationTests {
	
	@Autowired
	Suppliers suppliers;
	
	@Autowired
	ReferenceTables referenceTables;
	
	private static Integer SERIAL_NO = 1074;

	@Test
	void getSupplierSuccesfulTest() throws JsonProcessingException {
//		ObjectMapper onjMapper = new ObjectMapper(); 
//		String supplierJson = onjMapper.writeValueAsString(suppliers.getSupplier(196)); 
//		System.out.println(supplierJson);
		
		System.out.println(suppliers.getSupplier(196));
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
		
		Supplier supplier = new Supplier();
		supplier.setName("tets supplier " + SERIAL_NO);
//		supplier.setSupplyCategories(supplyCategories);
		ContactDetails contactDetails = new ContactDetails();
		for(int i=0; i<5; i++) {
			Phone phone = new Phone();
			phone.setName("phone" + i);
			contactDetails.getPhones().add(phone);
		}
		PaymentAccount paymentAccount = new PaymentAccount();
		
		BankAccount bankAccount = new BankAccount();
		bankAccount.setOwnerName("ownerName" + SERIAL_NO);
		bankAccount.setAccountNo("accountNo" + SERIAL_NO);
		BankBranch bankBranch = new BankBranch();
		bankBranch.setId(1);
		bankAccount.setBranch(bankBranch);
		paymentAccount.setBankAccount(bankAccount);
		contactDetails.getPaymentAccounts().add(paymentAccount);
		supplier.setContactDetails(contactDetails);

		CompanyContact companyContact;
		Person person;
		for(int i=0; i<5; i++) {
			companyContact = new CompanyContact();
			person = new Person();
			person.setName("person" + i);
			companyContact.setPerson(person);
			supplier.getCompanyContacts().add(companyContact);
		}
		suppliers.addSupplier(supplier);
		
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
