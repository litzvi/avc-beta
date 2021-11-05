/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.enums.SequenceIdentifier;
import com.avc.mis.beta.entities.settings.UOM;
import com.avc.mis.beta.utilities.ProgramSequence;

/**
 * @author Zvi
 *
 */
public interface SettingsRepository extends BaseRepository<LinkEntity> {

	@Query("select m from UOM m ")
	List<UOM> findAllUOM();
	

	@Query("select s from ProgramSequence s where s.identifier = :sequenceIdentifier ")
	ProgramSequence findSequence(SequenceIdentifier sequenceIdentifier);

	
}
