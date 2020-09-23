/**
 * 
 */
package com.avc.mis.beta.entities.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "COMPANY_CONTACTS")
public class CompanyContact extends ObjectEntityWithId {
	
	@ToString.Exclude
	@JsonBackReference(value = "company_companyContacts")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId", updatable = false, nullable = false)
	private Company company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId", updatable = false, nullable = false)
	@NotNull(message = "Internal failure: trying to add company contact without person", groups = OnPersist.class)
	private Person person;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "positionId")
	private CompanyPosition position;
	
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}

	/**
	 * Sets the company reference
	 * @param company need to be instance of Company
	 */
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof Company) {
			this.setCompany((Company)referenced);
		}
		else if(referenced instanceof Person) {
			this.setPerson((Person)referenced);
		}
		else {
			throw new ClassCastException("Referenced object dosen't match CompanyContact references");
		}
	}

}
