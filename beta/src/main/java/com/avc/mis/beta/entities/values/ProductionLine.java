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
 * Production line entity - includes a unique name for every production line.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="PRODUCTION_LINES")
public class ProductionLine extends ValueEntity {

}
