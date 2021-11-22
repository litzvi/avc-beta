/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Process for using inventory without processing.
 * e.g. use, fix inventory or throw away.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "INVENTORY_USES")
@PrimaryKeyJoinColumn(name = "processId")
public class InventoryUse extends RelocationProcess {

}
