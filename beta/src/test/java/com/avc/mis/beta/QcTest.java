package com.avc.mis.beta;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
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
		CashewStandardDTO standard = new CashewStandardDTO();
		String standardOrganization = "VinaControl";
		standard.setStandardOrganization(standardOrganization);
		standard.setValue("Whole Vina Control");
		standard.setDamage(new RawDamage());
		standard.setDefects(new RawDefects());
		Item item = service.getItem();
		Set<BasicValueEntity<Item>> items = new HashSet<>();
		items.add(new BasicValueEntity<Item>(item));
		standard.setItems(items);

		try {
			valueWriter.addCashewStandard(standard);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		CashewStandardDTO fetchedStandard = qualityChecks.getCashewStatndard(item.getId(), standardOrganization);

		System.out.println(fetchedStandard);
		valueWriter.permenentlyRemoveEntity(fetchedStandard);
	}

}
