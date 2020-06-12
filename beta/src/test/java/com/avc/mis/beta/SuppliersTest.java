package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.avc.mis.beta.dto.data.ContactDetailsDTO;
import com.avc.mis.beta.dto.data.FaxDTO;
import com.avc.mis.beta.dto.data.PhoneDTO;
import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.values.DataObjectWithName;
import com.avc.mis.beta.entities.data.Address;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Email;
import com.avc.mis.beta.entities.data.Fax;
import com.avc.mis.beta.entities.data.IdCard;
import com.avc.mis.beta.entities.data.PaymentAccount;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.Phone;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
//@Transactional
class SuppliersTest {
	
	private static final int NUM_ITEMS = 3;
	
	@Autowired TestService service;
	
	@Autowired
	private Suppliers suppliers;
	
	@Autowired
	private ValueTablesReader valueTablesReader;
	
	private final String SUPPLIER_NAME = " \t test supplier	 \t" + LocalDateTime.now().hashCode();
	
	public Supplier basicSupplier() {
		Supplier supplier = new Supplier();
		supplier.setName(SUPPLIER_NAME);
		supplier.setLocalName(" localName\t");
		supplier.setEnglishName("\t  englishName ");
		supplier.setLicense("\t  license \t");
		supplier.setTaxCode("  \ttaxCode\t ");
		supplier.setRegistrationLocation("\t   registrationLocation - any text  \t");
		return supplier;
	}
	
	public Supplier fullSupplier() {
		Supplier supplier = basicSupplier();
		
		//add all supply categories besides for one
		List<SupplyCategory> supplyCategories = valueTablesReader.getAllSupplyCategories();
		if(supplyCategories.isEmpty())
			fail("No Supply Categories in database");
		IntStream.range(1, supplyCategories.size())
			.forEach(i -> supplier.getSupplyCategories().add(supplyCategories.get(i)));
		
		//add phones
		Phone[] phones = new Phone[NUM_ITEMS];
		for(int i=0; i<phones.length; i++) {
			phones[i] = new Phone();
			phones[i].setValue(" phone " + i) ;
		}
		supplier.getContactDetails().setPhones(phones);
		
		//add faxes
		Fax[] faxes = new Fax[NUM_ITEMS];
		for(int i=0; i<faxes.length; i++) {
			faxes[i] = new Fax();
			faxes[i].setValue(" fax " + i) ;
		}
		supplier.getContactDetails().setFaxes(faxes);
		
		//add emails
		Email[] emails = new Email[NUM_ITEMS];
		for(int i=0; i<emails.length; i++) {
			emails[i] = new Email();
			emails[i].setValue(" email " + i + "	  	") ;
		}
		supplier.getContactDetails().setEmails(emails);
		
		//add address
		Address address = new Address();
		List<City> cities = valueTablesReader.getAllCities();
		if(cities.isEmpty())
			fail("No Cities in database");
		address.setCity(cities.get(0));
		address.setStreetAddress("streetAddress");
		supplier.getContactDetails().setAddresses(new Address[] {address});
		
		//add payment accounts
		PaymentAccount[] paymentAccounts = new PaymentAccount[NUM_ITEMS];
		List<BankBranch> branches = valueTablesReader.getAllBankBranches();
		if(branches.isEmpty())
			fail("No Bank Branches in database");
		BankBranch branch = branches.get(0);
		for(int i=0; i<paymentAccounts.length; i++) {
			paymentAccounts[i] = new PaymentAccount();
			BankAccount bankAccount = new BankAccount();
			bankAccount.setAccountNo("account " + i);
			bankAccount.setOwnerName("owner name " + i);			
			bankAccount.setBranch(branch);
			paymentAccounts[i].setBankAccount(bankAccount);
		}
		supplier.getContactDetails().setPaymentAccounts(paymentAccounts);
		
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
	
//	@Disabled
	@Test
	void suppliersTest() {
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
		SupplierDTO expected = new SupplierDTO(supplier, true);
		expected.setName(supplier.getName().trim()); //check that name is trimmed
		suppliers.addSupplier(supplier);
		SupplierDTO actual = null;
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test adding supplier with white spaces added to all info fields");
		
		//try adding supplier with duplicate name
		Supplier duplicateSupplier = basicSupplier();
		try {
			suppliers.addSupplier(duplicateSupplier);
			fail("should throw exception for duplicate supplier name");
		}catch(DataIntegrityViolationException e) {
			System.out.println(e.getMessage());
		}
		
		//edit main supplier info
		supplier.setLocalName("new localName\t");
		supplier.setEnglishName("\tnew  englishName ");
		supplier.setLicense("\tnew  license \t");
		supplier.setTaxCode("new  \ttaxCode\t ");
		supplier.setRegistrationLocation("\tnew   registrationLocation  \t");
		expected = new SupplierDTO(supplier, true);
		suppliers.editSupplierMainInfo(supplier);
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test editing supplier with white spaces added to all info fields");
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		//add, remove supply categories
		supplier = fullSupplier();
		expected = new SupplierDTO(supplier, true);
		suppliers.addSupplier(supplier);
		Set<SupplyCategory> categories = supplier.getSupplyCategories();
		if(categories.size() < 2)
			fail("Not enough supplier categories for add and remove test");
		Iterator<SupplyCategory> it = categories.iterator();
		SupplyCategory removedCategory = it.next();
		SupplyCategory addedCategory = it.next();
		categories.remove(removedCategory);
		categories.remove(addedCategory);
		supplier = suppliers.editSupplierMainInfo(supplier);
		categories = supplier.getSupplyCategories();
		categories.add(addedCategory);
		expected.getSupplyCategories().remove(removedCategory);
		suppliers.editSupplierMainInfo(supplier);
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test adding and removing supply categories");
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		//add supplier with full details
		supplier = fullSupplier();
		expected = new SupplierDTO(supplier, true);
		suppliers.addSupplier(supplier);
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test adding supplier contact details");
		suppliers.permenentlyRemoveSupplier(supplier.getId());
		
		//add supplier with full details add, remove and update a phones and faxes.
		supplier = fullSupplier();
		expected = new SupplierDTO(supplier, true);
		suppliers.addSupplier(supplier);
		ContactDetails contactDetails = supplier.getContactDetails();
		ContactDetailsDTO contactDetailsDTO = expected.getContactDetails();
		
		Phone[] phones = contactDetails.getPhones();
		Phone removedPhone = phones[0];		
		contactDetailsDTO.getPhones().remove(new PhoneDTO(removedPhone));
		Phone addedPhone = new Phone(); 
		addedPhone.setValue("added phone"); 
		addedPhone.setOrdinal(removedPhone.getOrdinal()); //when ordinal is null, Sorted set comparison isn't working
		phones[0] = addedPhone;
		contactDetailsDTO.getPhones().add(new PhoneDTO(addedPhone));
		Phone updatedPhone = phones[1]; 
		contactDetailsDTO.getPhones().remove(new PhoneDTO(updatedPhone));
		updatedPhone.setValue("updated value");
		contactDetailsDTO.getPhones().add(new PhoneDTO(updatedPhone));
		contactDetails.setPhones(phones);
		
		Fax[] faxes = contactDetails.getFaxes();
		Fax removedFax = faxes[0]; 
		contactDetailsDTO.getFaxes().remove(new FaxDTO(removedFax));
		Fax addedFax = new Fax(); 
		addedFax.setValue("added fax"); 
		addedFax.setOrdinal(removedFax.getOrdinal()); //when ordinal is null, Sorted set comparison isn't working
		faxes[0] = addedFax;
		contactDetailsDTO.getFaxes().add(new FaxDTO(addedFax));
		Fax updatedFax = faxes[1]; 
		contactDetailsDTO.getFaxes().remove(new FaxDTO(updatedFax));
		updatedFax.setValue("updated value");
		contactDetailsDTO.getFaxes().add(new FaxDTO(updatedFax));
		contactDetails.setFaxes(faxes);

		suppliers.editContactInfo(contactDetails);
		actual = suppliers.getSupplier(supplier.getId());
		assertEquals(expected, actual, "Failed test add, remove and update phone, fax and email");
		suppliers.permenentlyRemoveSupplier(supplier.getId());

		
		
	}
		
	private Supplier buildSupplier(String name) {
		Supplier supplier = new Supplier();
		supplier.setName(name);
		
		List<SupplyCategory> supplyCategories = valueTablesReader.getAllSupplyCategories();
		supplyCategories.forEach(category -> supplier.getSupplyCategories().add(category));
		
		ContactDetails contactDetails = new ContactDetails();
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
	void insertSupplierIsSuccessfulTest() {	
		
		//not adding an empty phone/email/fax/address
		//address without a city
		//adding non existing city
		//inserting 2 accounts with the same info
		//never crash the program
		//check if can add existing bank account
		//check if can add existing person to company contact
		
	}
	

}
