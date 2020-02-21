package com.avc.mis.beta;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dao.ReferenceTables;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dataobjects.ContactDetails;
import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dataobjects.SupplyCategory;

@SpringBootTest
class BetaApplicationTests {
	
	@Autowired
	Suppliers suppliers;
	
	@Autowired
	ReferenceTables referenceTables;
	
	private static Integer SERIAL_NO = 1053;

	@Test
	void editSupplierSuccesfulTest() {
		
	}
	
	@Test
	void insertSupplierIsSuccessfulTest() {	
				
		
//		HashSet<SupplyCategory> supplyCategories = new HashSet<>();
//		supplyCategories.addAll(referenceTables.getAllSupplyCategories());

		Supplier supplier = new Supplier();
		supplier.setName("tets supplier " + SERIAL_NO);
//		supplier.setSupplyCategories(supplyCategories);
		ContactDetails contactDetails = new ContactDetails();
		contactDetails.setPhones(new String[]{"99999", "8888", "222222"});
		System.out.println("hello");
		supplier.setContactDetails(contactDetails);
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
		
		SupplyCategory category = new SupplyCategory();
		category.setName("Bags " + SERIAL_NO);
		referenceTables.insertSupplyCategory(category);
		
	}

}
