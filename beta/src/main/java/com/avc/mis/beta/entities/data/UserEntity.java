/**
 * 
 */
package com.avc.mis.beta.entities.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ObjectDataEntity;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "USERS")
public class UserEntity extends ObjectDataEntity {
	
	@EqualsAndHashCode.Include
	@Id
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	@MapsId
	private Person person;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "positionId")
	private CompanyPosition position;

	@Override
	public boolean isLegal() {
		return person != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "UserEntity has to reference a person";
	}
}
