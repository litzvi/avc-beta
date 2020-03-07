/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.avc.mis.beta.dataobjects.interfaces.Insertable;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "COMPANY_CONTACTS")
public class CompanyContact implements Insertable {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

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
	
	/**
	 * @return
	 */
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return person != null && person.isLegal();
	}
	
	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException("Compony contact has to reference legal person (person name not blank");
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
