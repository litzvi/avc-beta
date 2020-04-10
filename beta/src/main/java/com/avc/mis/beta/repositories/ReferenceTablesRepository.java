/**
 * 
 */
package com.avc.mis.beta.repositories;

import com.avc.mis.beta.entities.ValueEntity;

/**
 * Spring repository for accessing lists of {@link ValueEntity} entities, 
 * that usually serve as constants referenced by user recorded data.
 * 
 * @author Zvi
 *
 */
public interface ReferenceTablesRepository extends BaseRepository<ValueEntity> {

}
