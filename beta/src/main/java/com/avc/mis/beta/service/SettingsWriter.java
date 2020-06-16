/**
 * 
 */
package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.entities.settings.UOM;

/**
 * @author Zvi
 *
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class SettingsWriter {

	@Autowired private DeletableDAO dao;
	
	public UOM editUOM(UOM uom) {
		return dao.editEntity(uom);
	}
}
