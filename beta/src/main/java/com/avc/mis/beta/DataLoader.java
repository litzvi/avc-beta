/**
 * 
 */
package com.avc.mis.beta;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.service.SettingsWriter;
import com.avc.mis.beta.service.Users;

/**
 * @author Zvi
 *
 */
@Component
public class DataLoader implements ApplicationRunner {

	@Autowired private Users users;
	
	@Autowired private SettingsWriter settingsWriter;
	
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	
		//insert an initial SYSTEM_MANAGER in order to have application access on startup
		List<String> names = args.getOptionValues("n");
		List<String> passwords = args.getOptionValues("p");
		if(names != null && !names.isEmpty() && passwords != null && !passwords.isEmpty()) {
			if(!users.contains(names.get(0))) {
				UserEntity user = new UserEntity();
				user.setUsername(names.get(0));
				user.setPassword(passwords.get(0));
				user.getRoles().add(Role.ROLE_SYSTEM_MANAGER);
				users.addUser(user);
				
				//add UOM - units of measure table data for conversion in queries
				settingsWriter.addAll(MeasureUnit.getAllUOM());
				
				//add process types
				List<ProcessType> processTypes = new ArrayList<ProcessType>();
				for(ProcessName processName: ProcessName.values()) {
					ProcessType processType = new ProcessType();
					processType.setProcessName(processName);
					processType.setValue(WordUtils.capitalizeFully(processName.name().replace('_', ' ')));
					processTypes.add(processType);
				}
				settingsWriter.addAll(processTypes);
			}			
		}
		
		//supply group
		
//		Map<MeasureUnit, Map<MeasureUnit, UOM>> uomMap = MeasureUnit.CONVERTION_MAP;
//		List<UOM> dbUOM = settingsTableReader.getAllUOM();
//		dbUOM.forEach(uom -> uomMap.get(uom.getFromUnit()).get(uom.getToUnit()).setId(uom.getId()));
//		settingsWriter.mergeAll(MeasureUnit.getAllUOM());
//		Map<MeasureUnit, Map<MeasureUnit, UOM>> dbMap = dbUOM.stream()
//				.collect(Collectors.groupingBy(UOM::getFromUnit, Collectors.toMap(UOM::getToUnit, u->u)));
//
////		List<UOM> uomList = MeasureUnit.getAllUOM();
//		Map<MeasureUnit, Map<MeasureUnit, UOM>> uomMap = MeasureUnit.CONVERTION_MAP;
//		
//		for(Entry<MeasureUnit, Map<MeasureUnit, UOM>> outerEntry: dbMap.entrySet()) {
//			MeasureUnit fromUnit = outerEntry.getKey();
//			for(Entry<MeasureUnit, UOM> innerEntry: outerEntry.getValue().entrySet()) {
//				MeasureUnit toUnit = innerEntry.getKey();
//				
//			}
//		}
	}

}
