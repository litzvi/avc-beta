/**
 * 
 */
package com.avc.mis.beta.entities.values;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ValueEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Company position entity - has a name-value for different types of positions.
 * e.g. manager, accountant etc.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="COMPANY_POSITIONS")
public class CompanyPosition extends ValueEntity {
	
}
