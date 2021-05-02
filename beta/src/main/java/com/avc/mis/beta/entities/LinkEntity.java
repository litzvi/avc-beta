/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class representing entities who aren't data or value entities but are rather 
 * internal program structure information, either as glue between entities or instructions
 * for business protocol. e.g. ContactDetails, ProcessManagement etc.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class LinkEntity extends BaseEntity {
	
}
