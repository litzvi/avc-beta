/**
 * 
 */
package com.avc.mis.beta;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.service.ProcessInfoWriter;

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
