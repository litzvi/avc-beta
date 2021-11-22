package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.avc.mis.beta.dto.data.AddressDTO;
import com.avc.mis.beta.dto.data.BankAccountDTO;
import com.avc.mis.beta.dto.data.CompanyContactDTO;
import com.avc.mis.beta.dto.data.EmailDTO;
import com.avc.mis.beta.dto.data.FaxDTO;
import com.avc.mis.beta.dto.data.PaymentAccountDTO;
import com.avc.mis.beta.dto.data.PersonDTO;
import com.avc.mis.beta.dto.data.PhoneDTO;
import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.link.ContactDetailsDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.SupplyCategoryDTO;
import com.avc.mis.beta.service.Suppliers;

@SpringBootTest
//@Transactional
class SuppliersTest {
	
	private static final int NUM_ITEMS = 3;
	
	@Autowired private TestService service;	
	@Autowired private Suppliers suppliers;	
	
	private final String SUPPLIER_NAME = " \t test supplier	 \t" + LocalDateTime.now().hashCode();
	
	public SupplierDTO basicSupplier() {
		SupplierDTO supplier = new SupplierDTO();
		supplier.setName(SUPPLIER_NAME);
		supplier.setLocalName(" localName\t");
		supplier.setEnglishName("\t  englishName ");
		supplier.setLicense("\t  license \t");
		supplier.setTaxCode("  \ttaxCode\t ");
		supplier.setRegistrationLocation("\t   registrationLocation - any text  \t");
		return supplier;
	}
	
	public SupplierDTO fullSupplier() {
		SupplierDTO supplier = basicSupplier();
		
		//add all supply categories besides for one
		supplier.getSupplyCategories().addAll(service.getSupplyCategories());
		supplier.getSupplyCategories().remove(service.getSupplycategory());
		
		supplier.setContactDetails(new ContactDetailsDTO());
		
		//add phones
		List<PhoneDTO> phones = new ArrayList<>();
		for(int i=0; i<NUM_ITEMS; i++) {
			PhoneDTO phone = new PhoneDTO();
			phones.add(phone);
			phone.setValue(" phone " + i) ;
		}
		supplier.getContactDetails().setPhones(phones);
		
		//add faxes
		List<FaxDTO> faxes = new ArrayList<FaxDTO>();
		for(int i=0; i<NUM_ITEMS; i++) {
			FaxDTO fax = new FaxDTO();
			faxes.add(fax);
			fax.setValue(" fax " + i) ;
		}
		supplier.getContactDetails().setFaxes(faxes);
		
		//add emails
		List<EmailDTO> emails = new ArrayList<EmailDTO>();
		for(int i=0; i<NUM_ITEMS; i++) {
			EmailDTO email = new EmailDTO();
			emails.add(email);
			email.setValue(" email " + i + "	  	") ;
		}
		supplier.getContactDetails().setEmails(emails);
		
		//add address
		AddressDTO address = new AddressDTO();		
		address.setCity(service.getCity());
		address.setStreetAddress("streetAddress");
		supplier.getContactDetails().setAddresses(address);
		
		//add payment accounts
		List<PaymentAccountDTO> paymentAccounts = new ArrayList<PaymentAccountDTO>();
		
		BankBranchDTO branch = service.getBankBranch();
		for(int i=0; i<NUM_ITEMS; i++) {
			PaymentAccountDTO paymentAccount = new PaymentAccountDTO();
			paymentAccounts.add(paymentAccount);
			BankAccountDTO bankAccount = new BankAccountDTO();
			bankAccount.setAccountNo("account " + i);
			bankAccount.setOwnerName("owner name " + i);			
			bankAccount.setBranch(branch);
			paymentAccount.setBankAccount(bankAccount);
		}
		supplier.getContactDetails().setPaymentAccounts(paymentAccounts);
		
		//add company contacts
		Set<CompanyContactDTO> contacts = new HashSet<CompanyContactDTO>();
		for(int i=0; i<NUM_ITEMS; i++) {
			CompanyContactDTO contact = new CompanyContactDTO();
			contacts.add(contact);
			PersonDTO person = new PersonDTO();
			person.setName("person " + i);
			contact.setPerson(person);
			FaxDTO fax = new FaxDTO();
			fax.setValue("fax for person " + i);
			PhoneDTO phone = new PhoneDTO();
			phone.setValue("phone for person " + i);
			EmailDTO email = new EmailDTO();
			email.setValue("email for person " + i);
			ContactDetailsDTO contactDetails = new ContactDetailsDTO();
			contactDetails.setPhones(Arrays.asList(phone));
			contactDetails.setFaxes(Arrays.asList(fax));
			contactDetails.setEmails(Arrays.asList(email));
			person.setContactDetails(contactDetails);
		}
		supplier.setCompanyContacts(contacts);
		
		return supplier;
	}
	
//	@Disabled
	@Test
	void suppliersTest() {
		//supplier with null name
		SupplierDTO supplier = basicSupplier();
		supplier.setName(null);
		try {
			suppliers.addSupplier(supplier);
			fail("should trow exception for supplier with null name");
		} catch (ConstraintViolationException e) {
			System.out.println(e.getMessage());
		}
		
		//supplier with name of white spaces
		supplier.setName(" 	");
		try {
			suppliers.addSupplier(supplier);
			fail("should trow exception for supplier with blank name");
		} catch (ConstraintViolationException | InvalidDataAccessApiUsageException e) {
			System.out.println(e.getMessage());
		}
		
		//adding supplier
		supplier = basicSupplier();
//		SupplierDTO expected = new SupplierDTO(supplier, true);
		SupplierDTO expected = supplier;
		expected.setName(supplier.getName().trim()); //check that name is trimmed
		Integer supplierId = suppliers.addSupplier(supplier);
		SupplierDTO actual = suppliers.getSupplier(supplierId);
		assertEquals(expected, actual, "Failed test adding supplier with white spaces added to all info fields");
		supplier = actual;
//		fail("actual basic supplier: " + actual);
		
		//try adding supplier with duplicate name
		SupplierDTO duplicateSupplier = basicSupplier();
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
//		expected = new SupplierDTO(supplier, true);
		expected = supplier;
		suppliers.editSupplierMainInfo(supplier);
		actual = suppliers.getSupplier(supplierId);
		assertEquals(expected, actual, "Failed test editing supplier with white spaces added to all info fields");
		try {
			service.cleanupSupplier(supplierId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		//add, remove supply categories
		supplier = fullSupplier();
		supplierId = suppliers.addSupplier(supplier);
		supplier = suppliers.getSupplier(supplierId);
		Set<SupplyCategoryDTO> categories = supplier.getSupplyCategories();
		if(categories.size() < 2)
			fail("Not enough supplier categories for add and remove test");
		Iterator<SupplyCategoryDTO> it = categories.iterator();
		SupplyCategoryDTO removedCategory = it.next();
		SupplyCategoryDTO addedCategory = it.next();
		categories.remove(removedCategory);
		categories.remove(addedCategory);
		suppliers.editSupplierMainInfo(supplier);
		supplier = suppliers.getSupplier(supplierId);
		categories = supplier.getSupplyCategories();
		categories.add(addedCategory);
		expected = supplier;
		suppliers.editSupplierMainInfo(supplier);
		actual = suppliers.getSupplier(supplierId);
		assertEquals(expected, actual, "Failed test adding and removing supply categories");
		service.cleanupSupplier(supplierId);
		
		//add supplier with full details
		//TODO assert adding contactDetails referencing both company and person or non will fail
		supplier = fullSupplier();
		supplierId = suppliers.addSupplier(supplier);
		actual = suppliers.getSupplier(supplierId);
		supplier = actual;
		expected = actual;
		
		//check removing contacts
		expected.getContactDetails().getPaymentAccounts().stream().forEach(i -> suppliers.removeAccount(i.getId()));
		actual = suppliers.getSupplier(expected.getId());
		expected.getContactDetails().setPaymentAccounts(new ArrayList<PaymentAccountDTO>());
		assertEquals(expected, actual, "Failed test removing company contacts");
		supplier = actual;
		
		//check removing contacts
		supplier.getCompanyContacts().stream().forEach(i -> suppliers.removeContactPerson(i.getId()));
		actual = suppliers.getSupplier(supplierId);
		expected.setCompanyContacts(new HashSet<CompanyContactDTO>());
		assertEquals(expected, actual, "Failed test removing company contacts");
		service.cleanupSupplier(supplierId);
		
		//add supplier with full details add, remove and update a phones and faxes.
		supplier = fullSupplier();
		supplierId = suppliers.addSupplier(supplier);
		expected = suppliers.getSupplier(supplierId);
		ContactDetailsDTO contactDetails = expected.getContactDetails();
//		ContactDetailsDTO contactDetailsDTO = expected.getContactDetails();
		
		List<PhoneDTO> phones = contactDetails.getPhones();
		PhoneDTO removedPhone = phones.remove(0);		
//		contactDetailsDTO.getPhones().remove(new PhoneDTO(removedPhone));
		PhoneDTO addedPhone = new PhoneDTO(); 
		addedPhone.setValue("added phone"); 
		addedPhone.setOrdinal(removedPhone.getOrdinal()); //when ordinal is null, Sorted set comparison isn't working
//		phones[0] = addedPhone;
		phones.add(0, addedPhone);
		PhoneDTO updatedPhone = phones.get(0); 
//		contactDetailsDTO.getPhones().remove(new PhoneDTO(updatedPhone));
		updatedPhone.setValue("updated value");
//		contactDetailsDTO.getPhones().add(new PhoneDTO(updatedPhone));
//		contactDetails.setPhones(phones);
		
		List<FaxDTO> faxes = contactDetails.getFaxes();
		FaxDTO removedFax = faxes.remove(0); 
//		contactDetailsDTO.getFaxes().remove(new FaxDTO(removedFax));
		FaxDTO addedFax = new FaxDTO(); 
		addedFax.setValue("added fax"); 
		addedFax.setOrdinal(removedFax.getOrdinal()); //when ordinal is null, Sorted set comparison isn't working
//		faxes[0] = addedFax;
		faxes.add(0, addedFax);
		FaxDTO updatedFax = faxes.get(0); 
//		contactDetailsDTO.getFaxes().remove(new FaxDTO(updatedFax));
		updatedFax.setValue("updated value");
//		contactDetailsDTO.getFaxes().add(new FaxDTO(updatedFax));
//		contactDetails.setFaxes(faxes);

		suppliers.editContactInfo(contactDetails, supplierId);
		actual = suppliers.getSupplier(supplierId);
		assertEquals(expected, actual, "Failed test add, remove and update phone, fax and email");
		service.cleanupSupplier(supplierId);

	}
		
//	private Supplier buildSupplier(String name) {
//		Supplier supplier = new Supplier();
//		supplier.setName(name);
//		
//		supplier.getSupplyCategories().addAll(service.getSupplyCategories());
//		
//		ContactDetails contactDetails = new ContactDetails();
//		supplier.setContactDetails(contactDetails);
//		List<Phone> phones = new ArrayList<>();
//		List<Fax> faxes = new ArrayList<>();
//		List<Email> emails = new ArrayList<>();
//		for(int i=0; i<2; i++) {
//			Phone phone = new Phone();
//			Phone duplicate = new Phone();
//			Fax fax = new Fax();
//			Email email = new Email();
//			phone.setValue("phone" + i);
//			duplicate.setValue("phone" + i);
//			fax.setValue("fax" + i);
//			email.setValue("email" + i);
//			phones.add(phone);
//			phones.add(duplicate);
//			faxes.add(fax);
//			emails.add(email);
//			
//		}
//		contactDetails.setPhones(phones.toArray(new Phone[phones.size()]));
//		contactDetails.setFaxes(faxes.toArray(new Fax[faxes.size()]));
//		contactDetails.setEmails(emails.toArray(new Email[emails.size()]));
//		PaymentAccount paymentAccount = new PaymentAccount();		
//		BankAccount bankAccount = new BankAccount();
//		bankAccount.setOwnerName("ownerName" + name);
//		bankAccount.setAccountNo("accountNo" + name);
//		BankBranch bankBranch = new BankBranch();
//		bankBranch.setId(1);
//		bankAccount.setBranch(bankBranch);
//		paymentAccount.setBankAccount(bankAccount);
//		contactDetails.setPaymentAccounts(new PaymentAccount[] {paymentAccount});		
//
//		CompanyContact companyContact;
//		Person person;
//		for(int i=0; i<2; i++) {
//			companyContact = new CompanyContact();
//			person = new Person();
//			person.setName("person" + i);
//			IdCard idCard = new IdCard();
//			idCard.setIdNumber("id card" + i);
//			person.setIdCard(idCard);
//			companyContact.setPerson(person);
//			supplier.setCompanyContacts(new CompanyContact[] {companyContact});
//		}
//		
//		
//		return supplier;
//		
//	}
			
	
//	@Disabled
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
