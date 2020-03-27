/**
 * 
 */
package com.avc.mis.beta.entities.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private Person person;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "positionId")
	private CompanyPosition position;
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return person != null && person.isLegal();
	}
	
	/**
	 * Sets the company reference
	 * @param company need to be instance of Company
	 */
	@Override
	public void setReference(Object company) {
		this.setCompany((Company)company);
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Compony contact has to reference legal person (person name not blank";
	}

}
