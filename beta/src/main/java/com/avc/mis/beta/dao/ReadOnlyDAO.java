/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;

/**
 * Data access object for only reading but not modifying information in database.
 * 
 * @author Zvi
 *
 */
@Repository
public class ReadOnlyDAO extends ReadDAO {

}
