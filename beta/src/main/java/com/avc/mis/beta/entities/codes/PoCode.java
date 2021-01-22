/**
 * 
 */
package com.avc.mis.beta.entities.codes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.process.PoProcess;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Table(name = "ORDER_PO_CODES")
//@PrimaryKeyJoinColumn(name = "poCodeId")
//@DiscriminatorValue("po_code")
public class PoCode extends BasePoCode {

	
	/**
	 * Does nothing - display should be empty for normal po code
	 * @param display
	 */
	@Override
	public void setDisplay(String display) {}

}
