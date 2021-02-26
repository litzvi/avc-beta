/**
 * 
 */
package com.avc.mis.beta.entities.codes;

import java.util.Set;

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
@Table(name = "GENERAL_PO_CODES")
public class GeneralPoCode extends BasePoCode {

	@Override
	public void setId(Integer id) {
		super.setId(id);
		setCode(String.valueOf(id));
	}
}
