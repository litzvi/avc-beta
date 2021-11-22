/**
 * 
 */
package com.avc.mis.beta.entities.process.collectionItems;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.avc.mis.beta.entities.GeneralInfoEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a file uploaded for any general process.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="FILES")
public class ProcessFile extends GeneralInfoEntity {

	@Lob
	@Column(unique = true)
	private String address;
	
}
