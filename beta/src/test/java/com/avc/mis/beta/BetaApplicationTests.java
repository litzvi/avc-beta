package com.avc.mis.beta;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dao.*;
import com.avc.mis.beta.dataobjects.*;

@SpringBootTest
class BetaApplicationTests {
	
	@Autowired
	Suppliers suppliers;

	@Test
	void contextLoads() {
		
	}
	
	@Test
	void insertEmptySupplierIsSuccessful() {
		System.out.println(suppliers.getSupplierDetails());
		//Supplier supplier = new Supplier(null, "supplier11", null, null, null, null, null, null, null);
	}

}
