/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.avc.mis.beta.dataobjects.interfaces.KeyIdentifiable;
import com.avc.mis.beta.dataobjects.interfaces.Legible;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="BANK_BRANCHES")
@NamedQuery(name = "BankBranch.findAll", query = "select bb from BankBranch bb")
public class BankBranch implements Legible, KeyIdentifiable{
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String value;
	
//	@JsonManagedReference(value = "branch_bank")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "bankId", nullable = false)
	private Bank bank;
	
	public void setValue(String value) {
		this.value = value.trim();
	}
	
	protected boolean canEqual(Object o) {
		return KeyIdentifiable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue()) && getBank() != null;
	}
	
	@PrePersist @PreUpdate
	@Override
	public void preUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException("Branch name can't be blank and branch has to belong to a bank");
	}
}
