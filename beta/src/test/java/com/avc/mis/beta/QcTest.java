package com.avc.mis.beta;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.ValueWriter;

@SpringBootTest
public class QcTest {
	
	@Autowired TestService service;	
	@Autowired ValueWriter valueWriter;
	@Autowired QualityChecks qualityChecks;
	
	@Test
	void qcTest() {
		CashewStandard standard = new CashewStandard();
		String standardOrganization = "VinaControl";
		standard.setStandardOrganization(standardOrganization);
		Item item = service.getItem();
		standard.setItem(item);
		
		CashewStandardDTO standardDTO = qualityChecks.getCashewStatndard(item.getId(), standardOrganization);
		if(standardDTO == null) {
			valueWriter.addCashewStandard(standard);
			standardDTO = qualityChecks.getCashewStatndard(item.getId(), standardOrganization);
		}
		System.out.println(standardDTO);

	}

}
