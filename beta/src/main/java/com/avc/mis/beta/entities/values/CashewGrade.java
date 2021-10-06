/**
 * 
 */
package com.avc.mis.beta.entities.values;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.ValueInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Country entity with a name-value with it's cities.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="CASHEW_GRADES")
public class CashewGrade extends ValueEntity implements ValueInterface {
	
	
}
