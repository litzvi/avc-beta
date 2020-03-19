/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.avc.mis.beta.dto.CityDTO;
import com.avc.mis.beta.entities.BaseEntityNoId;

/**
 * @author Zvi
 *
 */
@RestResource(exported = false)
public interface BaseRepository<T extends BaseEntityNoId> extends Repository<T, Integer> {
	
	@Query("select new com.avc.mis.beta.dto.CityDTO(city.id, city.value, ctry.value) "
			+ "from City city "
			+ "join Country ctry ")
	List<CityDTO> findAllCities();
}
