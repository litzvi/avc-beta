/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.ContactEntity;

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
@Table(name="EMAILS")
public class Email extends ContactEntity {

	@Column(name = "email", nullable = false)
	@NotBlank(message = "Email value is mandetory")
//	@javax.validation.constraints.Email(message = "Email pattern not legal") //should be flexible for other data
	private String value;
	
	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);
	}
	
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
	@Override
	public void setReference(Object referenced) {
		this.setContactDetails((ContactDetails)referenced);
		
	}
	
}
