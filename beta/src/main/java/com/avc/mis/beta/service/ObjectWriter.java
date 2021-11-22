/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessDAO;
import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.dto.codes.GeneralPoCodeDTO;
import com.avc.mis.beta.dto.codes.PoCodeDTO;
import com.avc.mis.beta.dto.codes.ProductPoCodeDTO;
import com.avc.mis.beta.dto.codes.ShipmentCodeDTO;
import com.avc.mis.beta.dto.process.collectionItems.ProcessFileDTO;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.MixPoCode;
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.enums.SequenceIdentifier;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.collectionItems.ProcessFile;
import com.avc.mis.beta.repositories.ObjectTablesRepository;
import com.avc.mis.beta.utilities.ProgramSequence;

/**
 * 
 * @author Zvi
 *
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class ObjectWriter {
	
	@Autowired private ProcessDAO dao;
	@Autowired private DeletableDAO deletableDAO;
	@Autowired private ProcessReader processReader;
	@Autowired private SettingsTableReader settingsTableReader;
	@Autowired private ObjectTablesRepository objectTablesRepository;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public Integer addPoCode(ProductPoCodeDTO poCode) {
		return dao.addEntity(poCode, ProductPoCode::new);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public Integer addPoCode(GeneralPoCodeDTO poCode) {
		return dao.addEntity(poCode, GeneralPoCode::new);
	}
	
	public PoCodeDTO getPoCode(int poCodeId) {
		Optional<PoCodeDTO> poCode = objectTablesRepository.findPoCodeById(poCodeId);
		return poCode.orElseThrow(
				()->new IllegalArgumentException("No PO code with given id"));		
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editPoCode(ProductPoCodeDTO poCode) {
		dao.editEntity(poCode, ProductPoCode::new);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editPoCode(GeneralPoCodeDTO poCode) {
		dao.editEntity(poCode, GeneralPoCode::new);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public Integer addShipmentCode(ShipmentCodeDTO shipmentCode) {
		return dao.addEntity(shipmentCode.fillEntity(new ShipmentCode()));
	}
	
	public ShipmentCodeDTO getShipmentCode(Integer shipmentCodeId) {
		return objectTablesRepository.findShipmentCode(shipmentCodeId);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editShipmentCode(ShipmentCodeDTO shipmentCode) {
		dao.editEntity(shipmentCode.fillEntity(new ShipmentCode()));
	}	
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addProcessFile(ProcessFileDTO processFile) {
		dao.checkProcessEditablity(processFile.getProcessId());
		ProcessBasic<GeneralProcess> processBasic = processReader.getProcessesBasic(processFile.getProcessId());		
		dao.addEntity(processFile.fillEntity(new ProcessFile()), processBasic.getProcessClazz(), processBasic.getId());
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editProcessFile(ProcessFileDTO processFile) {
		dao.checkProcessEditablity(processFile.getProcessId());
		dao.editEntity(processFile.fillEntity(new ProcessFile()));
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void removeProcessFile(ProcessFileDTO processFile) {
		dao.checkProcessEditablity(processFile.getProcessId());
		deletableDAO.permenentlyRemoveEntity(ProcessFile.class, processFile.getProcessId());
	}


	
	//----------------------------Depricated------------------------------------------

	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void addMixPoCode(MixPoCode poCode) {
		dao.addEntity(poCode);
	}
	
	@Deprecated //The code is set by the user
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	private void addGeneralPoCode(GeneralPoCode poCode) {
		ProgramSequence sequence = settingsTableReader.getSequnce(SequenceIdentifier.GENERAL_PO_CODE);
		poCode.setCode(String.valueOf(sequence.getSequance()));	
		sequence.advance();
		dao.addEntity(poCode);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void editMixPoCode(MixPoCode poCode) {
		dao.editEntity(poCode);
	}	

}
