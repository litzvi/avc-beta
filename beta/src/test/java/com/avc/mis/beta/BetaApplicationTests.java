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
	void insertEmptySupplierIsSuccessfulTest() {
		SupplyCategory[] supplyCategories = new SupplyCategory[2];
		supplyCategories[0] = new SupplyCategory(1, null);
		supplyCategories[0].setId(1);
		supplyCategories[1].setId(2);
		Supplier supplier = new Supplier("supplier12", null, null, null, null, null, null, null, supplyCategories);
		suppliers.addSupplier(supplier);
	}

}
