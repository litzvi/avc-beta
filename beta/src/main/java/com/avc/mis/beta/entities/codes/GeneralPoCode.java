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
@Table(name = "GENERAL_PO_CODES")
public class GeneralPoCode extends BasePoCode {
	
}
