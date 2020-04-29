/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;

/**
 * Spring repository for accessing lists of {@link ValueEntity} entities, 
 * that usually serve as constants referenced by user recorded data.
 * 
 * @author Zvi
 *
 */
public interface ValueTablesRepository extends BaseRepository<ValueEntity> {

}
