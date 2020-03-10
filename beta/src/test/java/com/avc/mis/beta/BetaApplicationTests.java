package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

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
import com.avc.mis.beta.dto.FaxDTO;
import com.avc.mis.beta.dto.PhoneDTO;
import com.avc.mis.beta.dto.SupplierDTO;
import com.avc.mis.beta.dto.SupplierRow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
//@Transactional
class BetaApplicationTests {
	
	private final int NUM_ITEMS = 3;

	
	@Autowired
	Suppliers suppliers;
	
	@Autowired
	ReferenceTables referenceTables;
	
	private static Integer SERIAL_NO = 1173;
	private ObjectMapper objMapper = new ObjectMapper(); 
	
	private Supplier basicSupplier() {
		Supplier supplier = new Supplier();
		supplier.setName(" \t test supplier	 \t" + SERIAL_NO);
		supplier.setLocalName(" localName\t");
		supplier.setEnglishName("\t  englishName ");
		supplier.setLicense("\t  license \t");
		supplier.setTaxCode("  \ttaxCode\t ");
		supplier.setRegistrationLocation("\t   registrationLocation - any text  \t");
		return supplier;
	}
	
	private Supplier fullSupplier() {
		Supplier supplier = basicSupplier();
		//add supply categories
		List<SupplyCategory> supplyCategories = referenceTables.getAllSupplyCategories();
		supplyCategories.remove(0);
		supplyCategories.forEach(category -> supplier.getSupplyCategories().add(category));
		//add phones
		Phone[] phones = new Phone[NUM_ITEMS];
		for(int i=0; i<phones.length; i++) {
			phones[i] = new Phone();
			phones[i].setValue(" value " + i) ;
		}
		supplier.getContactDetails().setPhones(phones);
		//add faxes
		Fax[] faxes = new Fax[NUM_ITEMS];
		for(int i=0; i<faxes.length; i++) {
			faxes[i] = new Fax();
			faxes[i].setValue(" value " + i) ;
		}
		supplier.getContactDetails().setFaxes(faxes);
		//add emails
		Email[] emails = new Email[NUM_ITEMS];
		for(int i=0; i<emails.length; i++) {
			emails[i] = new Email();
			emails[i].setValue(" value " + i + "	  	") ;
		}
		supplier.getContactDetails().setEmails(emails);
		//add addresses
		//add company contacts
		CompanyContact[] contacts = new CompanyContact[NUM_ITEMS];
		for(int i=0; i<contacts.length; i++) {
			contacts[i] = new CompanyContact();
			Person person = new Person();
			person.setName("person " + i);
			contacts[i].setPerson(person);
			Fax fax = new Fax();
			fax.setValue("fax for person " + i);
			Phone phone = new Phone();
			phone.setValue("phone for person " + i);
			Email email = new Email();
			email.setValue("email for person " + i);
			person.getContactDetails().setPhones(new Phone[] {phone});
			person.getContactDetails().setFaxes(new Fax[] {fax});
			person.getContactDetails().setEmails(new Email[] {email});
			
		}
		supplier.setCompanyContacts(contacts);
		
		return supplier;
	}
	
	@Test
	void suppliersTest() throws JsonProcessingException {
		//supplier with null name
		Supplier supplier = basicSupplier();
		supplier.setName(null);
		try {
			suppliers.addSupplier(supplier);
			fail("should trow exception for supplier with null name");
		} catch (InvalidDataAccessApiUsageException e) {
			System.out.println(e.getMessage());
		}
		//supplier with name of white spaces
		supplier.setName(" 	");
		try {
			suppliers.addSupplier(supplier);
			fail("should trow exception for supplier with blank name");
		} catch (InvalidDataAccessApiUsageException e) {
			System.out.println(e.getMessage());
		}
		//adding supplier
		supplier = basicSupplier();
		SupplierDTO expected = new SupplierDTO(supplier);
		expected.setName(supplier.getName().trim());
		suppliers.addSupplier(supplier);
		SupplierDTO actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test adding supplier with white spaces added to all info fields");
		//try adding supplier with duplicate name
		Supplier dupSupplier = basicSupplier();
		try {
			suppliers.addSupplier(dupSupplier);
			fail("should throw exception for duplicate supplier name");
		}catch(DataIntegrityViolationException e) {
			System.out.println(e.getMessage());
		}
		//edit main supplier info
		supplier.setLocalName("new localName\t");
		supplier.setEnglishName("\tnew  englishName ");
		supplier.setLicense("\tnew  license \t");
		supplier.setTaxCode("new  \ttaxCode\t ");
		supplier.setRegistrationLocation("\tnew   registrationLocation - any text  \t");
		expected = new SupplierDTO(supplier);
		suppliers.editSupplierMainInfo(supplier);
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test adding supplier with white spaces added to all info fields");
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		//add, remove supply categories
		supplier = fullSupplier();
		expected = new SupplierDTO(supplier);
		suppliers.addSupplier(supplier);
		Set<SupplyCategory> categories = supplier.getSupplyCategories();
		SupplyCategory removedCategory = categories.iterator().next();
		SupplyCategory addedCategory = categories.iterator().next();
		categories.remove(removedCategory);
		categories.remove(addedCategory);
		suppliers.editSupplierMainInfo(supplier);
		categories = supplier.getSupplyCategories();
		categories.add(addedCategory);
		expected.getSupplyCategories().remove(removedCategory);
		suppliers.editSupplierMainInfo(supplier);
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test adding and removing supply categories");
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		//add supplier with full details
		supplier = fullSupplier();
		expected = new SupplierDTO(supplier);
		expected.getContactDetails().getPhones().forEach(phone -> phone.getValue().trim());
		expected.getContactDetails().getEmails().forEach(email -> email.getValue().trim());		
		suppliers.addSupplier(supplier);
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test adding supplier contact details");
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		//add supplier with full details add, remove and update a phone, fax and email
		supplier = fullSupplier();
		expected = new SupplierDTO(supplier);
		suppliers.addSupplier(supplier);
		Phone[] phones = supplier.getContactDetails().getPhones();
		Fax[] faxes = supplier.getContactDetails().getFaxes();
		Phone removedPhone = phones[0];
		Fax removedFax = faxes[0];
		Phone updatedPhone = phones[1];
		Fax updatedFax = faxes[1];
		expected.getContactDetails().getPhones().remove(new PhoneDTO(updatedPhone));
		expected.getContactDetails().getFaxes().remove(new FaxDTO(updatedFax));
		updatedPhone.setValue("updated value");
		updatedFax.setValue("updated value");
		Phone addedPhone = new Phone();
		Fax addedFax = new Fax();
		addedPhone.setValue("added phone 1");
		addedFax.setValue("added fax 1");
		suppliers.addEntity(addedPhone, supplier.getContactDetails());
		suppliers.addEntity(addedFax, supplier.getContactDetails());
		expected.getContactDetails().getPhones().add(new PhoneDTO(addedPhone));
		expected.getContactDetails().getFaxes().add(new FaxDTO(addedFax));
		suppliers.removeEntity(removedPhone);
		suppliers.removeEntity(removedFax);
		expected.getContactDetails().getPhones().remove(new PhoneDTO(removedPhone));
		expected.getContactDetails().getFaxes().remove(new FaxDTO(removedFax));
		suppliers.editEntity(updatedPhone);
		suppliers.editEntity(updatedFax);
		expected.getContactDetails().getPhones().add(new PhoneDTO(updatedPhone));
		expected.getContactDetails().getFaxes().add(new FaxDTO(updatedFax));
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test add, remove and update phone, fax and email");
	
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		
	}

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
		System.out.println(contactDetails);
		supplier.setContactDetails(contactDetails);
		List<Phone> phones = new ArrayList<>();
		List<Fax> faxes = new ArrayList<>();
		List<Email> emails = new ArrayList<>();
		for(int i=0; i<2; i++) {
			Phone phone = new Phone();
			Phone duplicate = new Phone();
			Fax fax = new Fax();
			Email email = new Email();
			phone.setValue("phone" + i);
			duplicate.setValue("phone" + i);
			fax.setValue("fax" + i);
			email.setValue("email" + i);
			phones.add(phone);
			phones.add(duplicate);
			faxes.add(fax);
			emails.add(email);
			
		}
		contactDetails.setPhones(phones.toArray(new Phone[phones.size()]));
		contactDetails.setFaxes(faxes.toArray(new Fax[faxes.size()]));
		contactDetails.setEmails(emails.toArray(new Email[emails.size()]));
		PaymentAccount paymentAccount = new PaymentAccount();		
		BankAccount bankAccount = new BankAccount();
		bankAccount.setOwnerName("ownerName" + name);
		bankAccount.setAccountNo("accountNo" + name);
		BankBranch bankBranch = new BankBranch();
		bankBranch.setId(1);
		bankAccount.setBranch(bankBranch);
		paymentAccount.setBankAccount(bankAccount);
		contactDetails.setPaymentAccounts(new PaymentAccount[] {paymentAccount});		

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
			supplier.setCompanyContacts(new CompanyContact[] {companyContact});
		}
		
		
		return supplier;
		
	}
	
	@Disabled
	@Test
	void editSupplierIsSuccessfulTest() {
		Supplier supplier = buildSupplier("remove test" + SERIAL_NO);
		suppliers.addSupplier(supplier);
		Integer id = supplier.getId();
		String addedSupplier = suppliers.getSupplier(id).toString();
		supplier.setEnglishName("englishName" + SERIAL_NO); // edit supplier basic info
		supplier.setName("edit name test" + SERIAL_NO); // edit supplier name
		List<SupplyCategory> categoriesToRemove = referenceTables.getAllSupplyCategories();
		categoriesToRemove.remove(0); categoriesToRemove.remove(0);
		supplier.getSupplyCategories().removeAll(categoriesToRemove);// remove categories besides for 2
		suppliers.editSupplierMainInfo(supplier);
		String editedSupplier = suppliers.getSupplier(id).toString();
		ContactDetails contactDetails = supplier.getContactDetails();
		System.out.println(contactDetails);
		Phone removed = contactDetails.getPhones()[0];
		Phone added = new Phone();
		added.setValue(removed.getValue());
//		contactDetails.getPhones().clear();
		removed.setValue("changed phone 0" + SERIAL_NO);
		contactDetails.setPhones(new Phone[] {removed, added});
//		contactDetails.getPhones().add(removed);
//		contactDetails.getPhones().add(added);
//		iterator.next();
//		iterator.remove();
//		iterator = contactDetails.getPhones().iterator();
//		iterator.next().setName("changed phone 0" + SERIAL_NO);	
		System.out.println(contactDetails);
		suppliers.editContactInfo(contactDetails);
		String changedContactDetails = suppliers.getSupplier(id).toString();
		System.out.println(addedSupplier);
		System.out.println(editedSupplier);
		System.out.println(changedContactDetails);
//		suppliers.removeSupplier(id);
//		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> suppliers.getSupplier(id));
		
		
	}
	
	@Disabled
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
	
	@Disabled
	@Test
	void insertSupplyCategoriesTest() {
		
//		SupplyCategory category = new SupplyCategory();
//		category.setName("Bags " + SERIAL_NO);
//		referenceTables.insertSupplyCategory(category);
		
	}

}
