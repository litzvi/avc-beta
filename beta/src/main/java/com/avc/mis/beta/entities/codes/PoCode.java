/**
 * 
 */
package com.avc.mis.beta.entities.codes;

import javax.persistence.Entity;
import javax.persistence.Table;

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
