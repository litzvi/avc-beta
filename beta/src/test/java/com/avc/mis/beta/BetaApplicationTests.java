package com.avc.mis.beta;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dataobjects.*;

@SpringBootTest
class BetaApplicationTests {

	@Test
	void contextLoads() {
		
	}
	
	@Test
	void insertEmptySupplierIsSuccessful() {
		Supplier supplier = new Supplier(null, "supplier11", null, null, null, null, null, null, null);
	}

}
