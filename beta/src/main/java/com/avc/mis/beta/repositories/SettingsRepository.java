/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.settings.UOM;

/**
 * @author Zvi
 *
 */
public interface SettingsRepository extends BaseRepository<LinkEntity> {

	@Query("select m from UOM m ")
	List<UOM> findAllUOM();

	
}
