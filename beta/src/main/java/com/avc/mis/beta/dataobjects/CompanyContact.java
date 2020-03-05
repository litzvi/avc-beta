/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@IdClass(CompanyContactPK.class)
@Entity
@Table(name = "COMPANY_CONTACTS")
public class CompanyContact implements Insertable {

//	@Id
//	@Column(name = "companyId")
//	private Integer id;

	@Id
	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "company_companyContacts")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId", updatable = false, nullable = false)
	private Company company;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId", updatable = false, nullable = false)
	private Person person;

	@ManyToOne
	@JoinColumn(name = "positionId")
	private CompanyPosition position;
	
	/**
	 * @return
	 */
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

//	@Column(columnDefinition = "boolean default true", nullable = false)
//	private boolean isActive = true;
//	
	


}
