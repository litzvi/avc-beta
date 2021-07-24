/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.values.PoCodeDTO;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.MixPoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.enums.SequenceIdentifier;
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
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private ObjectTablesRepository objectTablesRepository;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addPoCode(PoCode poCode) {
		dao.addEntity(poCode);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addPoCode(GeneralPoCode poCode) {
		dao.addEntity(poCode);
	}
	
	public PoCodeDTO getPoCode(int poCodeId) {
		Optional<PoCodeDTO> poCode = objectTablesRepository.findPoCodeById(poCodeId);
		return poCode.orElseThrow(
				()->new IllegalArgumentException("No PO code with given id"));		
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editPoCode(PoCode poCode) {
		dao.editEntity(poCode);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editPoCode(GeneralPoCode poCode) {
		dao.editEntity(poCode);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addShipmentCode(ShipmentCode shipmentCode) {
		dao.addEntity(shipmentCode);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editShipmentCode(ShipmentCode shipmentCode) {
		dao.editEntity(shipmentCode);
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
		ProgramSequence sequence = dao.getSequnce(SequenceIdentifier.GENERAL_PO_CODE);
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
