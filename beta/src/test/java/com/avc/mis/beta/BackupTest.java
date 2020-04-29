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
import com.avc.mis.beta.repositories.jpa.BankRepository;
import com.avc.mis.beta.utilities.UpgradeService;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class BackupTest {


	@Autowired UpgradeService upgradeService;
	@Autowired BankRepository bankRepository;

	@Disabled
	@Test
	void backupTest() {
		Map<Class<? extends BaseEntity>, List<? extends BaseEntity>> backup = upgradeService.backup();
		bankRepository.deleteAll();
		try {
			upgradeService.restore(backup);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
