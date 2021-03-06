/**
 * 
 */
package com.avc.mis.beta;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.enums.Role;
import com.avc.mis.beta.entities.enums.SequenceIdentifier;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.service.SettingsWriter;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.utilities.ProgramSequence;

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
				List<ProcessType> processTypes = new ArrayList<>();
				for(ProcessName processName: ProcessName.values()) {
					ProcessType processType = new ProcessType();
					processType.setProcessName(processName);
					processType.setValue(WordUtils.capitalizeFully(processName.name().replace('_', ' ')));
					processTypes.add(processType);
				}
				settingsWriter.addAll(processTypes);
				
				//TODO should eventually be removed, so users can add there own production lines.
				//add production lines - temporary
//				List<ProductionLine> productionLines = new ArrayList<>();
//				for(ProductionFunctionality productionFunctionality: ProductionFunctionality.values()) {
//					ProductionLine productionLine = new ProductionLine();
//					productionLine.setProductionFunctionality(productionFunctionality);
//					productionLine.setValue(WordUtils.capitalizeFully(productionFunctionality.name().replace('_', ' ')));
//					productionLines.add(productionLine);
//				}
//				settingsWriter.addAll(productionLines);
				
				//add program sequences
				List<ProgramSequence> programSequences = new ArrayList<>();
				for(SequenceIdentifier identifier: SequenceIdentifier.values()) {
					ProgramSequence programSequence = new ProgramSequence();
					programSequence.setIdentifier(identifier);
					programSequences.add(programSequence);
				}
				settingsWriter.addAll(programSequences);
			}
			
		}
		
		//supply group

	}

}
