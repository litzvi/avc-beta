/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.process.PoCode;

/**
 * @author zvi
 *
 */
public class LoadedItem extends ProcessItem {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private PoCode poCode;	
	
	
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
}
