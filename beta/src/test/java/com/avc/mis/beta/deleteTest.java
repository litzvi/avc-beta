/**
 * 
 */
package com.avc.mis.beta;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.repositories.jpa.BankBranchRepository;
import com.avc.mis.beta.repositories.jpa.BankRepository;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.utilities.UpgradeService;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class deleteTest {

	private static Integer PO_CODE = 14107;

	@Autowired ProcessInfoWriter processInfoWriter;

	@Disabled
	@Test
	void deletePoTest() {
		System.out.println("starting delete for all po processes for " + PO_CODE);
		processInfoWriter.removeAllProcesses(PO_CODE);
		System.out.println("end of delete all processes for po code " + PO_CODE);
		

	}
	
}
