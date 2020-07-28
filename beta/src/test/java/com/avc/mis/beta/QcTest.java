package com.avc.mis.beta;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
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
		standard.setDamage(new RawDamage());
		standard.setDefects(new RawDefects());
		Item item = service.getItem();
		Set<Item> items = new HashSet<Item>();
		items.add(item);
		standard.setItems(items);

		try {
			valueWriter.addCashewStandard(standard);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		CashewStandardDTO standardDTO = qualityChecks.getCashewStatndard(item.getId(), standardOrganization);

		System.out.println(standardDTO);
		valueWriter.permenentlyRemoveEntity(standard);
	}

}
