/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 * Abstract class representing entities who aren't data or value entities but are rather 
 * internal program structure information, either as glue between entities are instructions
 * for business protocol. e.g. ContactDetails, ProcessTypeAlert
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@MappedSuperclass
public abstract class LinkEntity extends BaseEntity {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
}
