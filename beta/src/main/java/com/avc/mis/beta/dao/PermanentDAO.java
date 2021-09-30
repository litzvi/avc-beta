/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;

/**
 * Acts as a buffer for entities that shouldn't be removed or even soft deleted.
 * 
 * @author Zvi
 *
 */
@Repository
public class PermanentDAO extends DAO {
	
}
