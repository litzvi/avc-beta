/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.ContactEntity;
import com.avc.mis.beta.entities.GeneralInfoEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
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
	private String address;
	
	
	
	
}
